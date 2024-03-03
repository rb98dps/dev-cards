package com.devapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.sql.Timestamp;
import java.util.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "user")
@RequiredArgsConstructor
public class User {

    @Id
    @NonNull
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NonNull
    @Column(name = "first_name")
    private String firstName;

    @NonNull
    @Column(name = "last_name")
    private String lastName;

    @NonNull
    private String password;
    @NonNull
    private String email;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    @ToString.Exclude
    private Collection<Role> roles;


    @Column(name = "create_time")
    Timestamp createTime;

    @Column(name = "last_login_date")
    Timestamp lastLoginDate;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "favourite_subtopic",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "subtopic_id", referencedColumnName = "id"))
    @ToString.Exclude
    @JsonIgnore
    private Collection<SubTopic> favouriteSubTopics;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "favourite_topic",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "topic_id", referencedColumnName = "id"))
    @ToString.Exclude
    @JsonIgnore
    private Collection<Topic> favouriteTopics;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @JsonIgnore
    private Set<SubTopicProgress> subtopicProgresses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @JsonIgnore
    private Set<TopicProgress> topicProgresses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @JsonIgnore
    private Set<CardsProgress> userHistories = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @JsonIgnore
    private Set<UserProblem> userProblems = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "user_test",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "test_id"))
    @ToString.Exclude
    @JsonIgnore
    private Set<Test> tests = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void addRole(Role role) {
        if(this.roles==null)
            initiateRoles();

        roles.add(role);
    }

    private void initiateRoles() {
        this.roles = new ArrayList<>();
    }

    public void addRoles(List<Role> roles) {
        if(this.roles==null)
            initiateRoles();
        this.roles.addAll(roles);
    }

    private void initiateFavouriteSubTopic() {
        this.favouriteSubTopics = new ArrayList<>();
    }

    public void addFavouriteSubTopics(List<SubTopic> favouriteSubTopics) {
        if(this.favouriteSubTopics==null)
            initiateRoles();
        this.favouriteSubTopics.addAll(favouriteSubTopics);
    }

    public void addFavouriteSubTopic(SubTopic favouriteSubTopic) {
        if(this.favouriteSubTopics==null)
            initiateRoles();
        this.favouriteSubTopics.add(favouriteSubTopic);
    }


    private void initiateFavouriteTopic() {
        this.favouriteTopics = new ArrayList<>();
    }

    public void addFavouriteTopics(List<Topic> favouriteTopics) {
        if(this.favouriteTopics==null)
            initiateRoles();
        this.favouriteTopics.addAll(favouriteTopics);
    }

    public void addFavouriteTopic(Topic favouriteTopic) {
        if(this.favouriteTopics==null)
            initiateRoles();
        this.favouriteTopics.add(favouriteTopic);
    }
}
