package com.sunxn.news.webcrawler.service;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 从纯文本中提取出中文关键字
 *  （TextRank关键词提取）http://www.imooc.com/article/41987?block_id=tuijian_wz
 * @data: 2020/4/24 14:44
 * @author: xiaoNan
 */
@Component
public class TextRankKeyword {

    public static final int MAX_KEY_WORDS = 7;
    /**
     * 阻尼系数，一般取值为0.85
     */
    public static final float DAMPING_FACTOR = 0.85f;
    /**
     * 最大迭代次数
     */
    public static final int MAX_ITERATOR = 2000;
    /**
     * 极限值（图中任意一点的误差率小于给定的极限值时就可以达到收敛）
     */
    public static final float MIN_DIFF = 0.001f;

    public TextRankKeyword() {
        // jdk bug: Exception in thread "main" java.lang.IllegalArgumentException: Comparison method violates its general contract!
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
    }

    public String getKeyword(String title, String content) {
        List<Term> termList = HanLP.segment(title + content);
        List<String> wordList = new ArrayList<>();
        for (Term term: termList) {
            if (this.shouldInclude(term)) {
                wordList.add(term.word);
            }
        }
        Map<String, Set<String>> words = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        for (String word: wordList) {
            if (!words.containsKey(word)) {
                words.put(word, new HashSet<>());
            }
            /**
             * offer，add区别：
             *  一些队列有大小限制，因此如果想在一个满的队列中加入一个新项，多出的项就会被拒绝。
             *  调用add()方法抛出一个unchecked异常，调用offer只会返回false
             * poll，remove区别：
             *  remove和poll方法都是从队列中删除和返回第一个元素。remove方法的行为与Collection接口的版本相似，
             *  当队列为空时会抛异常，但是poll在空集合调用时不是抛出异常，只是返回null
             * peek，element区别：
             *  都是用于在队列的头部查询元素。在队列为空时，element抛出一个异常，peek返回null。
             */
            queue.offer(word);
            if (queue.size() > 5) {
                queue.poll();
            }
            for (String w: queue) {
                for (String w2: queue) {
                    if (w.equals(w2)) {
                        continue;
                    }
                    words.get(w).add(w2);
                    words.get(w2).add(w);
                }
            }
        }
        Map<String, Float> score = new HashMap<>();
        for (int i=0; i<MAX_ITERATOR; i++) {
            Map<String, Float> m = new HashMap<>();
            float max_diff = 0;
            for (Map.Entry<String, Set<String>> entry: words.entrySet()) {
                String key = entry.getKey();
                Set<String> value = entry.getValue();
                m.put(key, 1 - DAMPING_FACTOR);
                for (String other: value) {
                    int size = words.get(other).size();
                    if (key.equals(other) || size == 0) {
                        continue;
                    }
                    m.put(key, m.get(key) + DAMPING_FACTOR/size * (score.get(other) == null ? 0 : score.get(other)));
                }
                max_diff = Math.max(max_diff, Math.abs(m.get(key) - (score.get(key) == null ? 0 : score.get(key))));
            }

            score = m;
            if (max_diff <= MIN_DIFF) {
                break;
            }
        }

        List<Map.Entry<String, Float>> entryList = new ArrayList<>(score.entrySet());
        Collections.sort(entryList, ((o1, o2) -> (o1.getValue() - o2.getValue() > 0 ? -1 : 1)));
        List<Map.Entry<String, Float>> list = entryList.stream().filter(w -> w.getKey().length() > 1).collect(Collectors.toList());
        String result = "";
        int nKeyword = MAX_KEY_WORDS > list.size() ? list.size() : MAX_KEY_WORDS;
        for (int i=0; i<nKeyword; i++) {
            result += list.get(i).getKey() + " ";
        }
        System.out.println(result);
        return result;
    }

    /**
     * 是否应当将这个term纳入计算
     *  词性属于名词、动词、副词、形容词
     * @param term
     * @return  是否应当
     */
    private boolean shouldInclude(Term term) {
        return CoreStopWordDictionary.shouldInclude(term);
    }
}
