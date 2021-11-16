package ca.bazlur.visualizer.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest<T> {
    public SearchRequest(final T query) {
        this.query = query;
    }

    @Min(value = 0, message = "Paging must start with page 1")
    private int page = 0;
    @Min(value = 1, message = "You can request minimum 1 records")
    @Max(value = 100, message = "You can request maximum 100 records")
    private int size = 10;

    @Valid
    @NotNull
    private T query;

    private String sortBy;
}
