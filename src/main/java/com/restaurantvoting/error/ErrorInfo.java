package com.restaurantvoting.error;

public record ErrorInfo (String url, ErrorType type, String typeMessage, String[] details){
}