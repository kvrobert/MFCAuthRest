package com.mfctest.test_mfc.model;

import org.elasticsearch.ElasticsearchException;

public class MCFAuthorizerException extends ElasticsearchException {

    public MCFAuthorizerException(String message)
    {
        super(message);
    }

    public MCFAuthorizerException(String message, Throwable cause)
    {
        super(message,cause);
    }
}
