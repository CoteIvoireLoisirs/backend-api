package ca.deltagis.success.v1.domain.ports.in.exercice;

import java.util.Optional;

import ca.deltagis.success.v1.domain.core.entities.exercice.Exercice;


public interface IExerciceService {


      Optional<Exercice> getByCode(String code);

}
