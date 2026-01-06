package com.william.williamRestApi.mappers;

public interface Mapper<A,B>{
    B mapTo(A a);
    A mapFrom(B b);


}
