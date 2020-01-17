package org.dorax.utils;

import java.util.Objects;

/**
 * K V 对工具类
 *
 * @author wuchunfu
 * @date 2020-01-14
 */
public class KvPair<T1, T2> {

    public final T1 left;
    public final T2 right;

    public static <L, R> KvPair<L, R> of(final L left, final R right) {
        return new KvPair<>(left, right);
    }

    public KvPair(final T1 left, final T2 right) {
        this.left = left;
        this.right = right;
    }

    public T1 getLeft() {
        return left;
    }

    public T2 getRight() {
        return right;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final KvPair<?, ?> pair = (KvPair<?, ?>) o;
        return Objects.equals(left, pair.left) && Objects.equals(right, pair.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    @Override
    public String toString() {
        return "KvPair{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }
}
