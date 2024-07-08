package net.microfalx.lang;

import net.microfalx.lang.annotation.Name;

import static net.microfalx.lang.ArgumentUtils.requireNotEmpty;
import static net.microfalx.lang.StringUtils.capitalizeWords;

/**
 * Base class for all objects which can be identified, named and described.
 *
 * @param <T> the type of the identity
 */
public abstract class NamedIdentityAware<T> extends IdentityAware<T> implements Nameable, Descriptable {

    @Name
    private String name;
    private String description;

    @Override
    public final String getName() {
        return name;
    }

    protected final NamedIdentityAware<T> setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public final String getDescription() {
        return description;
    }

    protected NamedIdentityAware<T> setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Returns a new named instance with a different name.
     *
     * @param name the new name
     * @return a new instance
     */
    public NamedIdentityAware<T> withName(String name) {
        requireNotEmpty(name);
        NamedIdentityAware<T> copy = (NamedIdentityAware<T>) copy();
        copy.name = name;
        return copy;
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
            name = "Unknown";
        }

        public Builder<T> name(String name) {
            requireNotEmpty(name);
            this.name = name;
            return this;
        }

        public Builder<T> description(String description) {
            this.description = description;
            return this;
        }

        @Override
        public NamedIdentityAware<T> build() {
            NamedIdentityAware<T> instance = (NamedIdentityAware<T>) super.build();
            instance.name = name;
            instance.description = description;
            return instance;
        }
    }
}
