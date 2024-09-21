package com.snowflake_guide.tourmate.global.google_api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GooglePlaceDetailsResponseDto {
    private Result result;
    private String status;

    @Getter
    @Setter
    public static class Result {
        private List<AddressComponent> address_components;
        private String adr_address;
        private String business_status;
        private String formatted_address;
        private String formatted_phone_number;
        private Geometry geometry;
        private String icon;
        private String icon_background_color;
        private String icon_mask_base_uri;
        private String international_phone_number;
        private String name;
        private OpeningHours opening_hours;
        private List<Photo> photos;
        private String place_id;
        private PlusCode plus_code;
        private double rating;
        private String reference;
        private List<Review> reviews;
        private List<String> types;
        private String url;
        private int user_ratings_total;
        private int utc_offset;
        private String vicinity;
        private String website;
    }

    @Getter
    @Setter
    public static class AddressComponent {
        private String long_name;
        private String short_name;
        private List<String> types;
    }

    @Getter
    @Setter
    public static class Geometry {
        private Location location;
        private Viewport viewport;
    }

    @Getter
    @Setter
    public static class Location {
        private double lat;
        private double lng;
    }

    @Getter
    @Setter
    public static class Viewport {
        private Location northeast;
        private Location southwest;
    }

    @Getter
    @Setter
    public static class OpeningHours {
        private boolean open_now;
        private List<Period> periods;
        private List<String> weekday_text;
    }

    @Getter
    @Setter
    public static class Period {
        private Time open;
        private Time close;
    }

    @Getter
    @Setter
    public static class Time {
        private int day;
        private String time;
    }

    @Getter
    @Setter
    public static class Photo {
        private int height;
        private List<String> html_attributions;
        private String photo_reference;
        private int width;
    }

    @Getter
    @Setter
    public static class PlusCode {
        private String compound_code;
        private String global_code;
    }

    @Getter
    @Setter
    public static class Review {
        private String author_name;
        private String author_url;
        private String language;
        private String profile_photo_url;
        private double rating;
        private String relative_time_description;
        private String text;
        private long time;
    }
}
