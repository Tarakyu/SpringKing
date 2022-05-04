package jpabook.jpashop.tabler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class TablerController {

    private final EntityManager em;

    @GetMapping("/admin")
    public String admin(Model model) {
        Metamodel metamodel = em.getMetamodel();
        List<Class<?>> entities = new ArrayList<>();
        for (EntityType<?> entityType : metamodel.getEntities()) {
            Class<?> javaType = entityType.getJavaType();
            entities.add(javaType);
        }
        List<Object> results = new ArrayList<>();
        for (Class<?> entity : entities) {
            String className = entity.getName();
            List<Object> result = (List<Object>) em.createQuery("select o from " + className + " o" , entity).getResultList();
            results.addAll(result);
        }
        model.addAttribute("list", results);
        return "admin";
    }
}
