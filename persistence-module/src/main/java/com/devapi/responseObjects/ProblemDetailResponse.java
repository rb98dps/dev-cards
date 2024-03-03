package com.devapi.responseObjects;

import com.devapi.entities.ProblemDto;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProblemDetailResponse {
    ProblemDto problem;
    Integer orderNo;
}
