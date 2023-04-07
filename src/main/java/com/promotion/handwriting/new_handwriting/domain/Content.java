package com.promotion.handwriting.new_handwriting.domain;

import com.promotion.handwriting.new_handwriting.domain.type.ContentType;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Slf4j
public class Content {
    /**
     * Content id is depended on UUID.randomUUID()
     */
    @Id
    @Column(name = "id", nullable = false)
    private String id;
    /**
     * image is entity class. but image is regarded as @Embeddable
     */
    @OneToMany(mappedBy = "contentId", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Nimage> images = new ArrayList<>();
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ContentType type;

    /**
     * change content title text.
     * if input string is null, this method is not working.
     *
     * @param title change title
     */
    public void changeTitleText(String title) {
        if (title == null) return;
        this.title = title;
    }

    /**
     * change content description text.
     * if input string is null, this method is not working.
     *
     * @param description change description
     */
    public void changeDescriptionText(String description) {
        if (description == null) return;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public List<Nimage> getImages() {
        return new ArrayList<>(this.images);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    /**
     * client have to create object using this method.
     *
     * @param type  this is content type. please input ContentType.INTRO, ContentType.CONTENT
     * @param title this is content title value.
     * @return return Builder instance.
     */
    public static Builder builder(ContentType type, String title) {
        return new Builder(type, title);
    }

    /**
     * this class provide a client to set optional values.
     */
    public static class Builder {
        private final Content content;

        public Builder(ContentType type, String title) {
            content = new Content();
            content.id = UUID.randomUUID().toString();
            content.title = title;
        }

        public Builder images(List<Nimage> images) {
            content.images.addAll(images);
            return this;
        }

        public Builder description(String description) {
            content.description = description;
            return this;
        }

        public Content build() {
            return content;
        }
    }
}



