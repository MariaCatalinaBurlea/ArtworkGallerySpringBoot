package artgallery.serviceImpl;

import artgallery.repository.ArtworkRepository;
import artgallery.repository.CategoryRepository;
import artgallery.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class DashboardServiceImplementation implements DashboardService {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ArtworkRepository artworkRepository;


    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        System.out.println("inside getCount");

        Map<String , Object> map = new HashMap<>();
        map.put("category" , categoryRepository.count());
        map.put("artwork" , artworkRepository.count());
        return new ResponseEntity<>(map , HttpStatus.OK);
    }
}
