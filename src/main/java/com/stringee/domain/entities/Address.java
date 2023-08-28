package com.stringee.domain.entities;

import lombok.Builder;
import lombok.Data;

/**
 * @author dautv@stringee.com on 8/12/2023
 */
@Data
@Builder
public class Address {
    private String type;
    private String number;
    private String alias;
}
