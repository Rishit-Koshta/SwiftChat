package com.rishit.SwiftChat.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedMessageResponse {

    private List<MessageResponse> messages;
    private int currentPage;
    private boolean hasNextPage;

}
