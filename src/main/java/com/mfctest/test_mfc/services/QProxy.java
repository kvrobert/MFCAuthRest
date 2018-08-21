package com.mfctest.test_mfc.services;

import com.mfctest.test_mfc.model.Token;

import java.util.List;
import java.util.Map;

public interface QProxy {

    List<Token> getAuthTokens(Map<String, String> domainMap);


}
