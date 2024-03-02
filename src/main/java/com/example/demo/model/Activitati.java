package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "activitati")
public class Activitati {
  private List<Activitate> activitate;

  @XmlElement(name = "activitate")
  public List<Activitate> getActivitate() {
    return activitate;
  }
}
