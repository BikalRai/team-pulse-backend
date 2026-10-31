package rai.nexus.team_pulse.common.config.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaginationMeta {
    private int page;
    private int perPage;
    private long total;
    private int totalPages;
}
