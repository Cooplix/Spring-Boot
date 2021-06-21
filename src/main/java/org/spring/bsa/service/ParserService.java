package org.spring.bsa.service;

import org.spring.bsa.dto.CacheDto;

import java.io.File;
import java.util.Map;

public class ParserService {
    //TODO
    public CacheDto[] parseCache(Map<String, File[]> queriedCache) {

        CacheDto[] cacheDto = new CacheDto[queriedCache.size()];
        int counter = 0;
        int cacheCounter = 0;


        return null;
    }
}