package com.blynder.blynder.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.apache.lucene.analysis.ngram.NGramFilterFactory;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "stream_categories")
@Indexed
@AnalyzerDef(name = "suggestionAnalyzer",
        tokenizer = @TokenizerDef(factory = WhitespaceTokenizerFactory.class), //
        filters = {
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = EdgeNGramFilterFactory.class, params = {
                        @Parameter(name = "minGramSize", value = "1"),
                        @Parameter(name = "maxGramSize", value = "40")})
        })
public class StreamCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private int id;
    @NotNull
    @Field(index = Index.YES, store = Store.YES, analyzer = @Analyzer(definition = "suggestionAnalyzer"))
    private String categoryName;
    @NotNull
    private String categoryImage;

    @OneToMany(mappedBy = "streamCategory")
    @NotNull
    private Set<Stream> streams;
}
