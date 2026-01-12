package br.com.redhat.proto;

import org.infinispan.protostream.annotations.Proto;

import java.util.List;

@Proto
public record MovieList(List<Movie> movies) {}
