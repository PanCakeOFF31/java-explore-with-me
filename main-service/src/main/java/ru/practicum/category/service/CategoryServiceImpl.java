package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.exception.CategoryFieldOccupiedException;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.common.component.CommonComponent;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CommonComponent commonComponent;
    private final CategoryRepository categoryRepository;

    private static final String CATEGORY_NAME_OCCUPIED = "Потенциальное нарушение целостности данных. Поле Category.name: '%s' уже занято.";

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) throws CategoryFieldOccupiedException {
        log.debug("CategoryServiceImpl - service.createCategory({})", newCategoryDto);

        toCreateFieldValidation(newCategoryDto.getName());

        Category categoryToCreate = CategoryMapper.mapToCategory(newCategoryDto);
        return CategoryMapper.mapToCategoryDto(categoryRepository.save(categoryToCreate));
    }

    private void toCreateFieldValidation(String name) {
        log.debug("CategoryServiceImpl - service.toCreateFieldValidation({})", name);

        Optional<Category> foundCategory = categoryRepository.findCategoryByName(name);

        if (foundCategory.isPresent())
            commonComponent.throwAndLog(() -> new CategoryFieldOccupiedException(commonComponent
                    .prepareMessage(CATEGORY_NAME_OCCUPIED, name)));
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(long catId, NewCategoryDto categoryDto) throws CategoryFieldOccupiedException {
        log.debug("CategoryServiceImpl - service.updateCategory({}, {})", catId, categoryDto);

        String newName = categoryDto.getName();
        toUpdateFieldValidation(catId, newName);

        Category categoryToUpdate = commonComponent.getCategoryById(catId);
        categoryToUpdate.setName(newName);

        return CategoryMapper.mapToCategoryDto(categoryRepository.save(categoryToUpdate));
    }

    private void toUpdateFieldValidation(long catId, String name) {
        log.debug("CategoryServiceImpl - service.toUpdateFieldValidation({}, {})", catId, name);

        if (name == null || name.isBlank())
            return;

        Optional<Category> foundCategory = categoryRepository.findCategoryByName(name);

        if (foundCategory.isPresent() && foundCategory.get().getId() != catId)
            commonComponent.throwAndLog(() -> new CategoryFieldOccupiedException(commonComponent
                    .prepareMessage(CATEGORY_NAME_OCCUPIED, name)));
    }

    @Override
    @Transactional
    public void deleteCategory(long catId) {
        log.debug("CategoryServiceImpl - service.deleteCategory({})", catId);
        commonComponent.categoryExists(catId);
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto getCategoryById(long catId) {
        log.debug("CategoryServiceImpl - service.getCategoryById({})", catId);
        return CategoryMapper.mapToCategoryDto(commonComponent.getCategoryById(catId));
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        log.debug("CategoryServiceImpl - service.getCategories({}, {})", from, size);
        Pageable pageable = commonComponent.definePageable(from, size);
        return CategoryMapper.mapToCategoryDto(categoryRepository.findAll(pageable));
    }
}
