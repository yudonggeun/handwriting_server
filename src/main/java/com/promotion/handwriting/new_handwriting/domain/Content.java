package com.promotion.handwriting.new_handwriting.domain;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Slf4j
public class Content {
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

    /**
     * change content title text.
     * if input string is null, this method is not working.
     * @param title
     */
    public void changeTitleText(String title) {
        if(title == null) return;
        this.title = title;
    }

    /**
     * change content description text.
     * if input string is null, this method is not working.
     * @param description
     */
    public void changeDescriptionText(String description) {
        if(description == null) return;
        this.description = description;
    }
    public void addImage(Nimage image){
       if(image == null) return;
       this.images.add(image);
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
     * @param id primary key, this parameter is using in database primary key.
     * @param title this is content title value.
     * @return return Builder instance.
     */
    public static Builder builder(String id, String title) {
        return new Builder(id, title);
    }

    /**
     * this class provide a client to set optional values.
     */
    public static class Builder {
        private Content content;

        public Builder(String id, String title) {
            content = new Content();
            content.id = id;
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
