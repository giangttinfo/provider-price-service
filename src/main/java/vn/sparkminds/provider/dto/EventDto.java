package vn.sparkminds.provider.dto;

import java.util.List;

import lombok.Data;

@Data
public class EventDto {
    private String method;
    private List<String> params;
    private int id;
}
