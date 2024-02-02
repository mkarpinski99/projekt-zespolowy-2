package com.blynder.blynder.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.ngram.NGramFilterFactory;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Parameter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "users")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Indexed
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique=true)
    @NotNull
    @Field(index = Index.YES, store = Store.YES, analyzer = @Analyzer(definition = "suggestionAnalyzer"))
    private String username;

    @NotNull
    private String password;


    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonSetter
    public void setPassword(String password) {
        this.password = password;
    }


    @JsonIgnore
    private String authorities = "ROLE_USER";

    private int views;

    @JsonProperty("avatar_path")
    private String avatarPath;

    public String getAuthorities() {
        return authorities;
    }

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    private Stream stream;

    @JsonIgnore
    @OneToMany(mappedBy="userBeingFollowed",  cascade = CascadeType.REMOVE)
    private List<FollowInfo> followers;

    @JsonIgnore
    @OneToMany(mappedBy="followingUser", cascade = CascadeType.REMOVE)
    private List<FollowInfo> following; //todo dodalem notnull, ale sensowniejsza walidacja by sie przydala

    @OneToMany(mappedBy = "timeoutedUser")
    @JsonIgnore
    private Set<TimeoutInfo> userTimeoutedIn;

    @Column(columnDefinition = "boolean default false")
    private Boolean isBanned = false;

    private Date bannedUntil;

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean b) {
        isBanned = b;
    }

    @ManyToMany
    @JsonIgnore
    private Set<Stream> moderatorOn;


}
