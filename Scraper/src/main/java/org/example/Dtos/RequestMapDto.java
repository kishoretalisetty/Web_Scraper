package org.example.Dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestMapDto {
  private int  total_count;
  private long order_id;
  private List<Car> cars;
  private String humanized_results_count;

  @ToString.Exclude
  private String results_html;
}
