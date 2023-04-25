package com.juliusbaer.codility.embedded;

import oracle.ons.Closable;

public interface EmeddedLocalDatabase extends Closable {
    String getUsername();

    String getPassword();

    String getUrl();

}
