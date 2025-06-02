package com.practice.review.application.selection;

public final class Selections {

    private Selections() {}

    public static Selection first(int limit) {
        return new SelectionImpl(limit);
    }

    private static class SelectionImpl implements Selection {
        private final int limit;

        public SelectionImpl(int limit) {
            this.limit = limit;
        }

        @Override
        public int getLimit() {
            return limit;
        }
    }
}
