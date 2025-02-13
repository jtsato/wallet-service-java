package io.github.jtsato.walletservice.core.domains.transactions.model;

import java.util.Arrays;

public enum Type {

    DEPOSIT {
        @Override
        public String getMessageKey() {
            return "enum-type-deposit";
        }

    },

    WITHDRAWAL {
        @Override
        public String getMessageKey() {
            return "enum-type-withdrawal";
        }
    },

    TRANSFER {
        @Override
        public String getMessageKey() {
            return "enum-type-transfer";
        }
    };

    public abstract String getMessageKey();

    public boolean is(final Type other) {
        return equals(other);
    }

    public boolean isNot(final Type other) {
        return !is(other);
    }

    public boolean in(final Type... others) {
        return Arrays.asList(others).contains(this);
    }

    public boolean notIn(final Type... others) {
        return Arrays.stream(others).noneMatch(this::equals);
    }
}