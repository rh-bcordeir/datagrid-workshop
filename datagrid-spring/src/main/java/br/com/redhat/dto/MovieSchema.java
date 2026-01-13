package br.com.redhat.dto;

import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.protostream.annotations.ProtoSchema;

@ProtoSchema(includeClasses = { Movie.class, MovieList.class }, className = "MovieSchemaImpl")
public interface MovieSchema extends GeneratedSchema {
}
