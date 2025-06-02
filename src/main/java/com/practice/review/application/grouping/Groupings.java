package com.practice.review.application.grouping;


import java.util.UUID;

public final class Groupings {

    private Groupings() {}

    public static Grouping byOrganizationId(UUID organizationId) {
        return new GroupingByOrganization(organizationId);
    }

    public static Grouping byCity(Integer city) {
        return new GroupingByCity(city);
    }

    public static Grouping byAuthor(UUID authorId) {
        return new GroupingByAuthor(authorId);
    }

    public static class GroupingByOrganization implements Grouping {
        private final UUID organizationId;

        public GroupingByOrganization(UUID organizationId) {
            this.organizationId = organizationId;
        }

        public UUID getOrganizationId() {
            return organizationId;
        }

        @Override
        public GroupingType getType() {
            return GroupingType.BY_ORGANIZATION;
        }
    }

    public static class GroupingByCity implements Grouping {
        private final Integer city;

        public GroupingByCity(Integer city) {
            this.city = city;
        }

        public Integer getCity() {
            return city;
        }

        @Override
        public GroupingType getType() {
            return GroupingType.BY_CITY;
        }
    }

    public static class GroupingByAuthor implements Grouping {
        private final UUID authorId;

        public GroupingByAuthor(UUID authorId) {
            this.authorId = authorId;
        }

        public UUID getAuthorId() {
            return authorId;
        }

        @Override
        public GroupingType getType() {
            return GroupingType.BY_AUTHOR;
        }
    }
}
