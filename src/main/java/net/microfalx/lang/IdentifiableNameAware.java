package net.microfalx.lang;

import static net.microfalx.lang.StringUtils.capitalizeWords;

/**
 * Base class for all objects which can be identified, named and described
 *
 * @param <T> the type of the identity
 */
public abstract class IdentifiableNameAware<T> extends IdentityAware<T> implements Nameable, Descriptable {

    private String name;
    private String description;

    @Override
    public final String getName() {
        return name;
    }

    protected final IdentifiableNameAware<T> setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public final String getDescription() {
        return description;
    }

    protected IdentifiableNameAware<T> setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * A builder class.
     *
     * @param <T> the type of the identity
     */
    public static abstract class Builder<T> extends IdentityAware.Builder<T> {

        private String name;
        private String description;

        public Builder(T id) {
            super(id);
            this.name = capitalizeWords(ObjectUtils.toString(id));
        }

        public Builder() {
        }

        public Builder<T> name(String name) {
            this.name = name;
            return this;
        }

        public Builder<T> description(String description) {
            this.description = description;
            return this;
        }

        @Override
        public IdentifiableNameAware<T> build() {
            IdentifiableNameAware<T> instance = (IdentifiableNameAware<T>) super.build();
            instance.name = name;
            instance.description = description;
            return instance;
        }
    }
}
