package br.com.redhat.proto;

import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.protostream.annotations.ProtoSchema;

@ProtoSchema(includeClasses =  Movie.class)
public interface MovieSchema extends GeneratedSchema {
}
