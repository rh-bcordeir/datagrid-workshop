package br.com.redhat.proto;

import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.protostream.annotations.ProtoSchema;

@ProtoSchema(includeClasses = {Movie.class, MovieList.class})
public interface MovieSchema extends GeneratedSchema {
}

