package br.com.redhat.proto;

import org.infinispan.protostream.annotations.Proto;

@Proto
public record Movie(String name, String director, String year, String genre) {
}
