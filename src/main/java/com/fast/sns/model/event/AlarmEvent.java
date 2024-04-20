package com.fast.sns.model.event;

import com.fast.sns.model.AlarmArgs;
import com.fast.sns.model.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmEvent {

    private AlarmType type;
    private AlarmArgs args;
    private Integer receiverUserId = 1;
}
