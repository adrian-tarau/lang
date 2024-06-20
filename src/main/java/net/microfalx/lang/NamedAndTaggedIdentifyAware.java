package net.microfalx.lang;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;
import static net.microfalx.lang.ArgumentUtils.requireNonNull;

/**
 * Base class for all objects which can be identified, named, tagged and described.
 *
 * @param <T> the type of the identity
 */
public class NamedAndTaggedIdentifyAware<T> extends NamedIdentityAware<T> {

    private Set<String> tags;

    /**
     * Returns the tags associated with this instance.
     *
     * @return a non-null instance
     */
    public Set<String> getTags() {
        return tags != null ? unmodifiableSet(tags) : Collections.emptySet();
    }

    /**
     * A builder class.
     *
     * @param <T> the type of the identity
     */
    public static abstract class Builder<T> extends NamedIdentityAware.Builder<T> {

        private Set<String> tags;

        public Builder(T id) {
            super(id);
        }

        public Builder() {
        }

        public Builder<T> tag(String tag) {
            requireNonNull(tag);
            if (tags == null) tags = new HashSet<>();
            tags.add(tag);
            return this;
        }

        public Builder<T> tags(Collection<String> tags) {
            requireNonNull(tags);
            if (this.tags == null) this.tags = new HashSet<>();
            this.tags.addAll(tags);
            return this;
        }

        @Override
        public NamedAndTaggedIdentifyAware<T> build() {
            NamedAndTaggedIdentifyAware<T> item = (NamedAndTaggedIdentifyAware<T>) super.build();
            item.tags = tags;
            return item;
        }
    }
}
