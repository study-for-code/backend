package goorm.spoco.domain.algorithm.service;

import goorm.spoco.domain.algorithm.domain.Algorithm;
import goorm.spoco.domain.algorithm.domain.AlgorithmStatus;
import goorm.spoco.domain.algorithm.dto.AlgorithmDTO;
import goorm.spoco.domain.algorithm.repository.AlgorithmRepository;
import goorm.spoco.global.error.exception.CustomException;
import goorm.spoco.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AlgorithmService {

    private final AlgorithmRepository algorithmRepository;

    public Algorithm save(Algorithm algorithm) {
        Optional<Algorithm> optionalAlgorithm = algorithmRepository.findByTitle(algorithm.getTitle());
        if(optionalAlgorithm.isPresent()){
            throw new CustomException(ErrorCode.DUPLICATE_OBJECT, algorithm.getTitle()+"은/는 이미 존재하는 타이틀입니다. ");
        }

        return algorithmRepository.save(algorithm);
    }

    public void delete(Long id) {
        Algorithm algorithm = algorithmRepository.findAlgorithmByAlgorithmIdAndAlgorithmStatus(id, AlgorithmStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, id + "에 해당하는 알고리즘이 존재하지 않습니다."));

        algorithm.delete();
        algorithmRepository.save(algorithm);
    }

    public Algorithm update(Long algorithmId, AlgorithmDTO algorithmDTO) {
        Algorithm algorithm = algorithmRepository.findAlgorithmByAlgorithmIdAndAlgorithmStatus(algorithmId, AlgorithmStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, algorithmId + "에 해당하는 알고리즘이 존재하지 않습니다."));

        algorithm.setTitle(algorithmDTO.getTitle());
        algorithm.setExplanation(algorithmDTO.getExplanation());

//        return algorithmRepository.save(algorithm);
        return algorithm;
    }

}
