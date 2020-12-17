package com.ro.core.data.mapper;

import com.ro.core.data.AbstractModel;
import com.ro.core.data.Identity;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class ReferenceDtoMapper {
  @PersistenceContext
  private EntityManager em;

  @ObjectFactory
  public <T extends AbstractModel> T resolve(Identity sourceDto, @TargetType Class<T> clazz) {
      if (sourceDto.getId() == null) {
        return makeInstance(clazz);
      }

      T entity = em.find(clazz, sourceDto.getId());
      return entity != null ? entity : makeInstance(clazz);
  }

  private <T> T makeInstance(Class<T> clazz) {
    try {
      return clazz.getDeclaredConstructor().newInstance();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}
