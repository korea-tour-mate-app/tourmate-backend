package com.snowflake_guide.tourmate.global.google_api.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class GooglePlacesAPIResponseDto {

    private List<Result> results;
    private String next_page_token;
    private String status;

    @Getter
    public static class Result {
        private String business_status;
        private String formatted_address;
        private Geometry geometry;
        private String icon;
        private String icon_background_color;
        private String icon_mask_base_uri;
        private String name;
        private OpeningHours opening_hours;
        private List<Photo> photos;
        private String place_id;
        private PlusCode plus_code;
        private int price_level;
        private double rating;
        private String reference;
        private List<String> types;
        private int user_ratings_total;

        @Getter
        public static class Geometry {
            private Location location;
            private Viewport viewport;

            @Getter
            public static class Location {
                private double lat;
                private double lng;
            }

            @Getter
            public static class Viewport {
                private Location northeast;
                private Location southwest;
            }
        }

        @Getter
        public static class OpeningHours {
            private boolean open_now;
        }

        @Getter
        public static class Photo {
            private int height;
            private List<String> html_attributions;
            private String photo_reference;
            private int width;
        }

        @Getter
        public static class PlusCode {
            private String compound_code;
            private String global_code;
        }
    }
}