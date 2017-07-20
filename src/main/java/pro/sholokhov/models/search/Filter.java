package pro.sholokhov.models.search;

import java.time.LocalDateTime;

public class Filter {

    private LocalDateTime fromDateTime;
    private LocalDateTime toDateTime;
    private Long fromAccountId;
    private Long toAccountId;

    private Filter() {}

    public static Builder builder() {
        return new Filter().new Builder();
    }

    //

    public LocalDateTime getFromDateTime() {
        return fromDateTime;
    }

    public LocalDateTime getToDateTime() {
        return toDateTime;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    //

    public class Builder {

        private Builder() {}

        public Builder withFromDateTime(LocalDateTime fromDateTime) {
            Filter.this.fromDateTime = fromDateTime;
            return this;
        }

        public Builder withToDateTime(LocalDateTime toDateTime) {
            Filter.this.toDateTime = toDateTime;
            return this;
        }

        public Builder withFromAccountId(Long fromAccountId) {
            Filter.this.fromAccountId = fromAccountId;
            return this;
        }

        public Builder withToAccountId(Long toAccountId) {
            Filter.this.toAccountId = toAccountId;
            return this;
        }

        public Filter build() {
            return Filter.this;
        }

    }

}
