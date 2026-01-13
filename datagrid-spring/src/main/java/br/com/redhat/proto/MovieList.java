package br.com.redhat.proto;

import org.infinispan.protostream.annotations.Proto;
import org.infinispan.protostream.annotations.ProtoField;

import java.util.List;

@Proto
public record MovieList(@ProtoField(number = 1) List<Movie> movies) {
}

