package br.com.redhat.proto;

import org.infinispan.protostream.annotations.Proto;
import org.infinispan.protostream.annotations.ProtoField;

@Proto
public record Movie(
        @ProtoField(number = 1) Long id,
        @ProtoField(number = 2) String name,
        @ProtoField(number = 3) String director,
        @ProtoField(number = 4) String year,
        @ProtoField(number = 5) String genre) {
}

