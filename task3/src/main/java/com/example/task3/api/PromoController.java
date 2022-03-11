package com.example.task3.api;

import com.example.task3.api.pojo.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.task3.database.PromoDao;
import com.example.task3.database.entities.Promo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
public class PromoController {
    private static final Logger logger = Logger.getLogger(PromoController.class.getName());

    private final PromoDao dao;

    public PromoController(PromoDao promoDao) {
        this.dao = promoDao;
    }

    @PostMapping(value = "/promo")
    public SimpleResponseWithId addPromo(@Valid @RequestBody PromoDto promo) {
        Map<String, PromoDao.ParamWithType> params = new HashMap<>();

        params.put("name", new PromoDao.ParamWithType(promo.name, Types.VARCHAR));
        promo.description.ifPresent(x -> params.put("description", new PromoDao.ParamWithType(promo.description.get(), Types.VARCHAR)));

        return new SimpleResponseWithId(
                dao.add(params, "promo"));
    }

    @GetMapping(value = "/promo")
    public List<PromoBaseDto> getAvailablePromos() {
        return dao.findAll().stream().map(PromoBaseDto::new).collect(Collectors.toList());
    }

    @GetMapping(value = "/promo/{id}")
    public PromoDto getPromoById(@PathVariable long id) {
        Optional<Promo> result = dao.findById(id);
        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "promo not found");
        }

        return new PromoDto(result.get());
    }

    @PutMapping(value = "/promo/{id}")
    public ResponseEntity editPromo(@PathVariable(name = "id") long id, @Valid @RequestBody PromoDtoForEdit promo) {
        Map<String, String> paramsMap = new HashMap<>();
        promo.name.ifPresent(x -> paramsMap.put("name", x));
        promo.description.ifPresent(x -> paramsMap.put("description", x));
        boolean res = dao.updatePromo(id, paramsMap);

        if (!res) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/promo/{id}")
    public ResponseEntity deletePromo(@PathVariable long id) {
        boolean res = dao.delete(id, "promo");
        if (!res) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/promo/{id}/participant")
    public SimpleResponseWithId addParticipant(@RequestBody ParticipantDto participant, @PathVariable(name = "id") long promoId) {
        return new SimpleResponseWithId(
                dao.add(Map.of("name", new PromoDao.ParamWithType(participant.name, Types.VARCHAR),
                        "promo_id", new PromoDao.ParamWithType(promoId, Types.BIGINT)), "participants"));
    }

    @DeleteMapping(value = "/promo/{promoId}/participant/{participantId}")
    public ResponseEntity deleteParticipant(@PathVariable(name = "promoId") long promoId,
                                            @PathVariable(name = "participantId") long participantId)
    {
        boolean res = dao.delete("participants", Map.of("id", participantId, "promo_id", promoId));
        if (!res) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/promo/{id}/prize")
    public SimpleResponseWithId addPrize(@PathVariable(name = "id") long promoId, @RequestBody PrizeDto prize) {
        return new SimpleResponseWithId(
                dao.add(Map.of("description", new PromoDao.ParamWithType(prize.description, Types.VARCHAR),
                                "promo_id", new PromoDao.ParamWithType(promoId, Types.BIGINT)), "prizes"));
    }

    @DeleteMapping(value = "/promo/{promoId}/prize/{prizeId}")
    public ResponseEntity deletePrize(@PathVariable(name = "promoId") long promoId,
                                            @PathVariable(name = "prizeId") long prizeId)
    {
        boolean res = dao.delete("prizes", Map.of("id", prizeId, "promo_id", promoId));
        if (!res) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().build();
    }

//    @PostMapping(value = "/promo/{id}/raffle")
//    public List<PromoResultDto> getResults(@PathVariable(name = "id") long promoId) {
//
//    }

    class SimpleResponseWithId {
        public final long id;

        @JsonCreator
        public SimpleResponseWithId(@JsonProperty long id) {
            this.id = id;
        }
    }
}
