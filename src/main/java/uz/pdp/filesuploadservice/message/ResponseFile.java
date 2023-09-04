package uz.pdp.filesuploadservice.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.pdp.filesuploadservice.entity.BaseEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseFile {
  private String name;
  private String url;
  private String type;
  private long size;

}
