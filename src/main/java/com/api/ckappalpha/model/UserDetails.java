package com.api.ckappalpha.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Full user representation including nested address and company.
 * Contains business logic methods for deriving values from the raw API response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDetails {

    private Integer id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;
    private Address address;
    private Company company;

    public String getFormattedAddress() {
        if (address == null) {
            return "";
        }
        return Stream.of(address.getStreet(), address.getSuite(),
                        formatCityZip(address.getCity(), address.getZipcode()))
                .filter(part -> part != null && !part.isBlank())
                .collect(Collectors.joining(", "));
    }

    public boolean isLocatedInNorthernHemisphere() {
        if (address == null || address.getGeo() == null || address.getGeo().getLat() == null) {
            return false;
        }
        return Double.parseDouble(address.getGeo().getLat()) > 0;
    }

    private static String formatCityZip(String city, String zipcode) {
        if (city == null || city.isBlank()) {
            return zipcode;
        }
        if (zipcode == null || zipcode.isBlank()) {
            return city;
        }
        return city + " " + zipcode;
    }
}
