package ru.practicum.common.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.category.exception.CategoryNotFoundException;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.compilation.exception.CompilationNotFoundException;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.exception.EventNotFoundException;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.like.exception.LikeNotFoundException;
import ru.practicum.like.model.Like;
import ru.practicum.like.model.LikeId;
import ru.practicum.like.repository.LikeRepository;
import ru.practicum.location.exception.LocationNotFoundException;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.LocationRepository;
import ru.practicum.partrequest.exception.ParticipationRequestNotFoundException;
import ru.practicum.partrequest.model.ParticipationRequest;
import ru.practicum.partrequest.repository.ParticipationRequestRepository;
import ru.practicum.rating.exception.RatingNotFoundException;
import ru.practicum.rating.model.Rating;
import ru.practicum.rating.model.RatingId;
import ru.practicum.rating.repository.RatingRepository;
import ru.practicum.user.exception.UserNotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommonComponentImplTest {
    @InjectMocks
    private CommonComponentImpl commonComponent;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private CompilationRepository compilationRepository;
    @Mock
    private ParticipationRequestRepository participationRequestRepository;
    @Mock
    private LikeRepository likeRepository;
    @Mock
    private RatingRepository ratingRepository;

    private final long userId = 1;
    private final long categoryId = 2;
    private final long locationId = 3;
    private final float latitude = 3.15F;
    private final float longitude = 3.15F;
    private final long eventId = 4;
    private final long compilationId = 5;
    private final long partRequestId = 6;
    private final boolean isLike = true;
    private final int ratingValue = 9;

    private Object[] onlyUserRepo;
    private Object[] onlyCategoryRepo;
    private Object[] onlyLocationRepo;
    private Object[] onlyEventRepo;
    private Object[] onlyCompilationRepo;
    private Object[] onlyParticipationRequestRepo;
    private Object[] onlyLikeRepo;
    private Object[] onlyRatingRepo;

    private static final String USER_NOT_FOUND = "User with id=%d was not found.";
    private static final String CATEGORY_NOT_FOUND = "Category with id=%d was not found.";
    private static final String LOCATION_NOT_FOUND = "Location with latitude=%f and longitude=%f was not found.";
    private static final String EVENT_NOT_FOUND = "Event with id=%d was not found.";
    private static final String PUBLISHED_EVENT_NOT_FOUND = "Published event with id=%d was not found.";
    private static final String COMPILATION_NOT_FOUND = "Compilation with id=%d was not found.";
    private static final String PARTICIPATION_REQUEST_NOT_FOUND = "Request with id=%d was not found.";
    private static final String LIKE_NOT_FOUND = "LIKE with id=(%d, %d) was not found.";
    private static final String RATING_NOT_FOUND = "Rating with User.id=%d and Event.id=%d was not found.";

    private User user;
    private Category category;
    private Location location;
    private Event event;
    private Compilation compilation;
    private ParticipationRequest request;
    private Like like;
    private Rating rating;

    @BeforeEach
    public void preTestInitialization() {
        onlyUserRepo = new Object[]{categoryRepository, locationRepository,
                eventRepository, compilationRepository, participationRequestRepository, likeRepository, ratingRepository};

        onlyCategoryRepo = new Object[]{userRepository, locationRepository,
                eventRepository, compilationRepository, participationRequestRepository, likeRepository, ratingRepository};

        onlyLocationRepo = new Object[]{userRepository, categoryRepository,
                eventRepository, compilationRepository, participationRequestRepository, likeRepository, ratingRepository};

        onlyEventRepo = new Object[]{userRepository, categoryRepository, locationRepository,
                compilationRepository, participationRequestRepository, likeRepository, ratingRepository};

        onlyCompilationRepo = new Object[]{userRepository, categoryRepository, locationRepository,
                eventRepository, participationRequestRepository, likeRepository, ratingRepository};

        onlyParticipationRequestRepo = new Object[]{userRepository, categoryRepository, locationRepository,
                eventRepository, compilationRepository, likeRepository, ratingRepository};

        onlyLikeRepo = new Object[]{userRepository, categoryRepository, locationRepository,
                eventRepository, compilationRepository, participationRequestRepository, ratingRepository};

        onlyRatingRepo = new Object[]{userRepository, categoryRepository, locationRepository,
                eventRepository, compilationRepository, participationRequestRepository, likeRepository};

        user = User.builder()
                .id(userId)
                .name("some name")
                .email("somemail@yandex.ru")
                .build();

        category = Category.builder()
                .id(categoryId)
                .name("some category name")
                .build();

        location = Location.builder()
                .id(locationId)
                .lon(latitude)
                .lat(longitude)
                .build();

        event = Event.builder()
                .id(eventId)
                .title("some title")
                .annotation("some annotation")
                .build();

        compilation = Compilation.builder()
                .id(compilationId)
                .title("some title")
                .pinned(true)
                .build();

        request = ParticipationRequest.builder()
                .id(partRequestId)
                .requester(user)
                .event(event)
                .build();

        like = Like.builder()
                .userId(userId)
                .eventId(eventId)
                .like(isLike)
                .build();

        rating = Rating.builder()
                .userId(userId)
                .eventId(eventId)
                .rating(ratingValue)
                .build();
    }

    @Test
    public void test_T0010_PS01_containsUserById_contains() {
        Mockito.when(userRepository.existsById(userId))
                .thenReturn(true);

        boolean result = commonComponent.containsUserById(userId);
        assertTrue(result);

        Mockito.verify(userRepository, Mockito.only()).existsById(userId);
        Mockito.verifyNoMoreInteractions(userRepository);

        Mockito.verifyNoInteractions(onlyUserRepo);
    }

    @Test
    public void test_T0010_NS01_containsUserById_notContains() {
        Mockito.when(userRepository.existsById(userId))
                .thenReturn(false);

        boolean result = commonComponent.containsUserById(userId);
        assertFalse(result);

        Mockito.verify(userRepository, Mockito.only()).existsById(userId);
        Mockito.verifyNoMoreInteractions(userRepository);

        Mockito.verifyNoInteractions(onlyUserRepo);
    }

    @Test
    public void test_T0020_PS01_userExists_contains() {
        Mockito.when(userRepository.existsById(userId))
                .thenReturn(true);

        assertDoesNotThrow(() -> commonComponent.userExists(userId));

        Mockito.verify(userRepository, Mockito.only()).existsById(userId);
        Mockito.verifyNoMoreInteractions(userRepository);

        Mockito.verifyNoInteractions(onlyUserRepo);
    }

    @Test
    public void test_T0020_NS01_userExists_notContains_throwsUserNotFoundException() {
        Mockito.when(userRepository.existsById(userId))
                .thenReturn(false);

        RuntimeException exception = assertThrows(UserNotFoundException.class, () -> commonComponent.userExists(userId));
        assertEquals(String.format(USER_NOT_FOUND, userId), exception.getMessage());

        Mockito.verify(userRepository, Mockito.only()).existsById(userId);
        Mockito.verifyNoMoreInteractions(userRepository);

        Mockito.verifyNoInteractions(onlyUserRepo);
    }

    @Test
    public void test_T0030_PS01_getUserById_contains() {
        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        User gotUser = commonComponent.getUserById(userId);

        assertNotNull(gotUser);
        assertEquals(user.getId(), gotUser.getId());
        assertEquals(user.getName(), gotUser.getName());
        assertEquals(user.getEmail(), gotUser.getEmail());

        Mockito.verify(userRepository, Mockito.only()).findById(userId);
        Mockito.verifyNoMoreInteractions(userRepository);

        Mockito.verifyNoInteractions(onlyUserRepo);
    }

    @Test
    public void test_T0030_NS01_getUserById_notContains_throwsUserNotFoundException() {
        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(UserNotFoundException.class, () -> commonComponent.getUserById(userId));
        assertEquals(String.format(USER_NOT_FOUND, userId), exception.getMessage());

        Mockito.verify(userRepository, Mockito.only()).findById(userId);
        Mockito.verifyNoMoreInteractions(userRepository);

        Mockito.verifyNoInteractions(onlyUserRepo);
    }

    @Test
    public void test_T0040_PS01_containsCategoryById_contains() {
        Mockito.when(categoryRepository.existsById(categoryId))
                .thenReturn(true);

        boolean result = commonComponent.containsCategoryById(categoryId);
        assertTrue(result);

        Mockito.verify(categoryRepository, Mockito.only()).existsById(categoryId);
        Mockito.verifyNoMoreInteractions(categoryRepository);

        Mockito.verifyNoInteractions(onlyCategoryRepo);
    }

    @Test
    public void test_T0040_NS01_containsCategoryById_notContains() {
        Mockito.when(categoryRepository.existsById(categoryId))
                .thenReturn(false);

        boolean result = commonComponent.containsCategoryById(categoryId);
        assertFalse(result);

        Mockito.verify(categoryRepository, Mockito.only()).existsById(categoryId);
        Mockito.verifyNoMoreInteractions(categoryRepository);

        Mockito.verifyNoInteractions(onlyCategoryRepo);
    }

    @Test
    public void test_T0050_PS01_categoryExists_contains() {
        Mockito.when(categoryRepository.existsById(categoryId))
                .thenReturn(true);

        assertDoesNotThrow(() -> commonComponent.categoryExists(categoryId));

        Mockito.verify(categoryRepository, Mockito.only()).existsById(categoryId);
        Mockito.verifyNoMoreInteractions(categoryRepository);

        Mockito.verifyNoInteractions(onlyCategoryRepo);
    }

    @Test
    public void test_T00520_NS01_categoryExists_notContains_throwsCategoryNotFoundException() {
        Mockito.when(categoryRepository.existsById(categoryId))
                .thenReturn(false);

        RuntimeException exception = assertThrows(CategoryNotFoundException.class, () -> commonComponent.categoryExists(categoryId));
        assertEquals(String.format(CATEGORY_NOT_FOUND, categoryId), exception.getMessage());

        Mockito.verify(categoryRepository, Mockito.only()).existsById(categoryId);
        Mockito.verifyNoMoreInteractions(categoryRepository);

        Mockito.verifyNoInteractions(onlyCategoryRepo);
    }

    @Test
    public void test_T0060_PS01_getCategoryById_contains() {
        Mockito.when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        Category gotCategory = commonComponent.getCategoryById(categoryId);

        assertNotNull(gotCategory);
        assertEquals(category.getId(), gotCategory.getId());
        assertEquals(category.getName(), gotCategory.getName());

        Mockito.verify(categoryRepository, Mockito.only()).findById(categoryId);
        Mockito.verifyNoMoreInteractions(categoryRepository);

        Mockito.verifyNoInteractions(onlyCategoryRepo);
    }

    @Test
    public void test_T0060_NS01_getCategoryById_notContains_throwsCategoryNotFoundException() {
        Mockito.when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(CategoryNotFoundException.class, () -> commonComponent.getCategoryById(categoryId));
        assertEquals(String.format(CATEGORY_NOT_FOUND, categoryId), exception.getMessage());

        Mockito.verify(categoryRepository, Mockito.only()).findById(categoryId);
        Mockito.verifyNoMoreInteractions(categoryRepository);

        Mockito.verifyNoInteractions(onlyCategoryRepo);
    }

    @Test
    public void test_T0070_PS01_containsLocationByLatAndLon_contains() {
        Mockito.when(locationRepository.existsByLatAndLon(latitude, longitude))
                .thenReturn(true);

        boolean result = commonComponent.containsLocationByLatAndLon(latitude, longitude);
        assertTrue(result);

        Mockito.verify(locationRepository, Mockito.only()).existsByLatAndLon(latitude, longitude);
        Mockito.verifyNoMoreInteractions(locationRepository);

        Mockito.verifyNoInteractions(onlyLocationRepo);
    }

    @Test
    public void test_T0070_NS01_containsLocationByLatAndLon_notContains() {
        Mockito.when(locationRepository.existsByLatAndLon(latitude, longitude))
                .thenReturn(false);

        boolean result = commonComponent.containsLocationByLatAndLon(latitude, longitude);
        assertFalse(result);

        Mockito.verify(locationRepository, Mockito.only()).existsByLatAndLon(latitude, longitude);
        Mockito.verifyNoMoreInteractions(locationRepository);

        Mockito.verifyNoInteractions(onlyLocationRepo);
    }

    @Test
    public void test_T0080_PS01_locationExists_contains() {
        Mockito.when(locationRepository.existsByLatAndLon(latitude, longitude))
                .thenReturn(true);

        assertDoesNotThrow(() -> commonComponent.locationExists(latitude, longitude));

        Mockito.verify(locationRepository, Mockito.only()).existsByLatAndLon(latitude, longitude);
        Mockito.verifyNoMoreInteractions(locationRepository);

        Mockito.verifyNoInteractions(onlyLocationRepo);
    }

    @Test
    public void test_T0080_NS01_locationExists_notContains_throwsLocationNotFoundException() {
        Mockito.when(locationRepository.existsByLatAndLon(latitude, longitude))
                .thenReturn(false);

        RuntimeException exception = assertThrows(LocationNotFoundException.class, () -> commonComponent.locationExists(latitude, longitude));
        assertEquals(String.format(LOCATION_NOT_FOUND, latitude, longitude), exception.getMessage());

        Mockito.verify(locationRepository, Mockito.only()).existsByLatAndLon(latitude, longitude);
        Mockito.verifyNoMoreInteractions(locationRepository);

        Mockito.verifyNoInteractions(onlyLocationRepo);
    }

    @Test
    public void test_T0090_PS01_getLocationByLatAndLon_contains() {
        Mockito.when(locationRepository.findByLatAndLon(latitude, longitude))
                .thenReturn(Optional.of(location));

        Location gotLocation = commonComponent.getLocationByLatAndLon(latitude, longitude);

        assertNotNull(gotLocation);
        assertEquals(location.getId(), gotLocation.getId());
        assertEquals(location.getLat(), gotLocation.getLat());
        assertEquals(location.getLon(), gotLocation.getLon());

        Mockito.verify(locationRepository, Mockito.only()).findByLatAndLon(latitude, longitude);
        Mockito.verifyNoMoreInteractions(locationRepository);

        Mockito.verifyNoInteractions(onlyLocationRepo);
    }

    @Test
    public void test_T0090_NS01_getLocationByLatAndLon_notContains_throwsLocationNotFoundException() {
        Mockito.when(locationRepository.findByLatAndLon(latitude, longitude))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(LocationNotFoundException.class, () -> commonComponent.getLocationByLatAndLon(latitude, longitude));
        assertEquals(String.format(LOCATION_NOT_FOUND, latitude, longitude), exception.getMessage());

        Mockito.verify(locationRepository, Mockito.only()).findByLatAndLon(latitude, longitude);
        Mockito.verifyNoMoreInteractions(locationRepository);

        Mockito.verifyNoInteractions(onlyLocationRepo);
    }

    @Test
    public void test_T0100_PS01_containsEventById_contains() {
        Mockito.when(eventRepository.existsById(eventId))
                .thenReturn(true);

        boolean result = commonComponent.containsEventById(eventId);
        assertTrue(result);

        Mockito.verify(eventRepository, Mockito.only()).existsById(eventId);
        Mockito.verifyNoMoreInteractions(eventRepository);

        Mockito.verifyNoInteractions(onlyEventRepo);
    }

    @Test
    public void test_T0100_NS01_containsEventById_notContains() {
        Mockito.when(eventRepository.existsById(eventId))
                .thenReturn(false);

        boolean result = commonComponent.containsEventById(eventId);
        assertFalse(result);

        Mockito.verify(eventRepository, Mockito.only()).existsById(eventId);
        Mockito.verifyNoMoreInteractions(eventRepository);

        Mockito.verifyNoInteractions(onlyEventRepo);
    }

    @Test
    public void test_T0100_PS01_containsPublishedEventById() {
        Mockito.when(eventRepository.existsByIdAndState(eventId, EventState.PUBLISHED))
                .thenReturn(true);

        boolean result = commonComponent.containsPublishedEventById(eventId);
        assertTrue(result);

        Mockito.verify(eventRepository, Mockito.only()).existsByIdAndState(eventId, EventState.PUBLISHED);
        Mockito.verifyNoMoreInteractions(eventRepository);

        Mockito.verifyNoInteractions(onlyEventRepo);
    }

    @Test
    public void test_T0100_NS01_containsPublishedEventById() {
        Mockito.when(eventRepository.existsByIdAndState(eventId, EventState.PUBLISHED))
                .thenReturn(false);

        boolean result = commonComponent.containsPublishedEventById(eventId);
        assertFalse(result);

        Mockito.verify(eventRepository, Mockito.only()).existsByIdAndState(eventId, EventState.PUBLISHED);
        Mockito.verifyNoMoreInteractions(eventRepository);

        Mockito.verifyNoInteractions(onlyEventRepo);
    }

    @Test
    public void test_T0110_PS01_eventExists_contains() {
        Mockito.when(eventRepository.existsById(eventId))
                .thenReturn(true);

        assertDoesNotThrow(() -> commonComponent.eventExists(eventId));

        Mockito.verify(eventRepository, Mockito.only()).existsById(eventId);
        Mockito.verifyNoMoreInteractions(eventRepository);

        Mockito.verifyNoInteractions(onlyEventRepo);
    }

    @Test
    public void test_T0110_NS01_eventExists_notContains_throwsEventNotFoundException() {
        Mockito.when(eventRepository.existsById(eventId))
                .thenReturn(false);

        RuntimeException exception = assertThrows(EventNotFoundException.class, () -> commonComponent.eventExists(eventId));
        assertEquals(String.format(EVENT_NOT_FOUND, eventId), exception.getMessage());

        Mockito.verify(eventRepository, Mockito.only()).existsById(eventId);
        Mockito.verifyNoMoreInteractions(eventRepository);

        Mockito.verifyNoInteractions(onlyEventRepo);
    }

    @Test
    public void test_T0110_PS01_publishedEventExists_contains() {
        Mockito.when(eventRepository.existsByIdAndState(eventId, EventState.PUBLISHED))
                .thenReturn(true);

        assertDoesNotThrow(() -> commonComponent.publishedEventExists(eventId));

        Mockito.verify(eventRepository, Mockito.only()).existsByIdAndState(eventId, EventState.PUBLISHED);
        Mockito.verifyNoMoreInteractions(eventRepository);

        Mockito.verifyNoInteractions(onlyEventRepo);
    }

    @Test
    public void test_T0110_NS01_publishedEventExists_notContains_throwsEventNotFoundException() {
        Mockito.when(eventRepository.existsByIdAndState(eventId, EventState.PUBLISHED))
                .thenReturn(false);

        RuntimeException exception = assertThrows(EventNotFoundException.class, () -> commonComponent.publishedEventExists(eventId));
        assertEquals(String.format(PUBLISHED_EVENT_NOT_FOUND, eventId), exception.getMessage());

        Mockito.verify(eventRepository, Mockito.only()).existsByIdAndState(eventId, EventState.PUBLISHED);
        Mockito.verifyNoMoreInteractions(eventRepository);

        Mockito.verifyNoInteractions(onlyEventRepo);
    }

    @Test
    public void test_T0120_PS01_getEventById_contains() {
        Mockito.when(eventRepository.findById(eventId))
                .thenReturn(Optional.of(event));

        Event gotEvent = commonComponent.getEventById(eventId);

        assertNotNull(gotEvent);
        assertEquals(event.getId(), gotEvent.getId());
        assertEquals(event.getTitle(), gotEvent.getTitle());
        assertEquals(event.getAnnotation(), gotEvent.getAnnotation());

        Mockito.verify(eventRepository, Mockito.only()).findById(eventId);
        Mockito.verifyNoMoreInteractions(eventRepository);

        Mockito.verifyNoInteractions(onlyEventRepo);
    }

    @Test
    public void test_T0120_NS01_getEventById_notContains_throwsEventNotFoundException() {
        Mockito.when(eventRepository.findById(eventId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(EventNotFoundException.class, () -> commonComponent.getEventById(eventId));
        assertEquals(String.format(EVENT_NOT_FOUND, eventId), exception.getMessage());

        Mockito.verify(eventRepository, Mockito.only()).findById(eventId);
        Mockito.verifyNoMoreInteractions(eventRepository);

        Mockito.verifyNoInteractions(onlyEventRepo);
    }

    @Test
    public void test_T0120_PS01_getPublishedEventById_contains() {
        Mockito.when(eventRepository.findByIdAndState(eventId, EventState.PUBLISHED))
                .thenReturn(Optional.of(event));

        Event gotEvent = commonComponent.getPublishedEventById(eventId);

        assertNotNull(gotEvent);
        assertEquals(event.getId(), gotEvent.getId());
        assertEquals(event.getTitle(), gotEvent.getTitle());
        assertEquals(event.getAnnotation(), gotEvent.getAnnotation());

        Mockito.verify(eventRepository, Mockito.only()).findByIdAndState(eventId, EventState.PUBLISHED);
        Mockito.verifyNoMoreInteractions(eventRepository);

        Mockito.verifyNoInteractions(onlyEventRepo);
    }

    @Test
    public void test_T0120_NS01_getPublishedEventById_notContains_throwsEventNotFoundException() {
        Mockito.when(eventRepository.findByIdAndState(eventId, EventState.PUBLISHED))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(EventNotFoundException.class, () -> commonComponent.getPublishedEventById(eventId));
        assertEquals(String.format(PUBLISHED_EVENT_NOT_FOUND, eventId), exception.getMessage());

        Mockito.verify(eventRepository, Mockito.only()).findByIdAndState(eventId, EventState.PUBLISHED);
        Mockito.verifyNoMoreInteractions(eventRepository);

        Mockito.verifyNoInteractions(onlyEventRepo);
    }

    @Test
    public void test_T0130_PS01_containsCompilationById_contains() {
        Mockito.when(compilationRepository.existsById(compilationId))
                .thenReturn(true);

        boolean result = commonComponent.containsCompilationById(compilationId);
        assertTrue(result);

        Mockito.verify(compilationRepository, Mockito.only()).existsById(compilationId);
        Mockito.verifyNoMoreInteractions(compilationRepository);

        Mockito.verifyNoInteractions(onlyCompilationRepo);
    }

    @Test
    public void test_T0130_NS01_containsCompilationById_notContains() {
        Mockito.when(compilationRepository.existsById(compilationId))
                .thenReturn(false);

        boolean result = commonComponent.containsCompilationById(compilationId);
        assertFalse(result);

        Mockito.verify(compilationRepository, Mockito.only()).existsById(compilationId);
        Mockito.verifyNoMoreInteractions(compilationRepository);

        Mockito.verifyNoInteractions(onlyCompilationRepo);
    }

    @Test
    public void test_T0140_PS01_compilationExists_contains() {
        Mockito.when(compilationRepository.existsById(compilationId))
                .thenReturn(true);

        assertDoesNotThrow(() -> commonComponent.compilationExists(compilationId));

        Mockito.verify(compilationRepository, Mockito.only()).existsById(compilationId);
        Mockito.verifyNoMoreInteractions(compilationRepository);

        Mockito.verifyNoInteractions(onlyCompilationRepo);
    }

    @Test
    public void test_T0140_NS01_compilationExists_notContains_throwsCompilationNotFoundException() {
        Mockito.when(compilationRepository.existsById(compilationId))
                .thenReturn(false);

        RuntimeException exception = assertThrows(CompilationNotFoundException.class, () -> commonComponent.compilationExists(compilationId));
        assertEquals(String.format(COMPILATION_NOT_FOUND, compilationId), exception.getMessage());

        Mockito.verify(compilationRepository, Mockito.only()).existsById(compilationId);
        Mockito.verifyNoMoreInteractions(compilationRepository);

        Mockito.verifyNoInteractions(onlyCompilationRepo);
    }

    @Test
    public void test_T0150_PS01_getCompilationById_contains() {
        Mockito.when(compilationRepository.findById(compilationId))
                .thenReturn(Optional.of(compilation));

        Compilation gotCompilation = commonComponent.getCompilationById(compilationId);

        assertNotNull(gotCompilation);
        assertEquals(compilation.getId(), gotCompilation.getId());
        assertEquals(compilation.getTitle(), gotCompilation.getTitle());

        Mockito.verify(compilationRepository, Mockito.only()).findById(compilationId);
        Mockito.verifyNoMoreInteractions(compilationRepository);

        Mockito.verifyNoInteractions(onlyCompilationRepo);
    }

    @Test
    public void test_T0150_NS01_getCompilationById_notContains_throwsCompilationNotFoundException() {
        Mockito.when(compilationRepository.findById(compilationId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(CompilationNotFoundException.class, () -> commonComponent.getCompilationById(compilationId));
        assertEquals(String.format(COMPILATION_NOT_FOUND, compilationId), exception.getMessage());

        Mockito.verify(compilationRepository, Mockito.only()).findById(compilationId);
        Mockito.verifyNoMoreInteractions(compilationRepository);

        Mockito.verifyNoInteractions(onlyCompilationRepo);
    }


    @Test
    public void test_T0160_PS01_containsParticipationRequestById_contains() {
        Mockito.when(participationRequestRepository.existsById(partRequestId))
                .thenReturn(true);

        boolean result = commonComponent.containsParticipationRequestById(partRequestId);
        assertTrue(result);

        Mockito.verify(participationRequestRepository, Mockito.only()).existsById(partRequestId);
        Mockito.verifyNoMoreInteractions(participationRequestRepository);

        Mockito.verifyNoInteractions(onlyParticipationRequestRepo);
    }

    @Test
    public void test_T0160_NS01_containsParticipationRequestById_notContains() {
        Mockito.when(participationRequestRepository.existsById(partRequestId))
                .thenReturn(false);

        boolean result = commonComponent.containsParticipationRequestById(partRequestId);
        assertFalse(result);

        Mockito.verify(participationRequestRepository, Mockito.only()).existsById(partRequestId);
        Mockito.verifyNoMoreInteractions(participationRequestRepository);

        Mockito.verifyNoInteractions(onlyParticipationRequestRepo);
    }

    @Test
    public void test_T0170_PS01_partrequestExists_contains() {
        Mockito.when(participationRequestRepository.existsById(partRequestId))
                .thenReturn(true);

        assertDoesNotThrow(() -> commonComponent.partrequestExists(partRequestId));

        Mockito.verify(participationRequestRepository, Mockito.only()).existsById(partRequestId);
        Mockito.verifyNoMoreInteractions(participationRequestRepository);

        Mockito.verifyNoInteractions(onlyParticipationRequestRepo);
    }

    @Test
    public void test_T0170_NS01_partrequestExists_notContains_throwsParticipationRequestNotFoundException() {
        Mockito.when(participationRequestRepository.existsById(partRequestId))
                .thenReturn(false);

        RuntimeException exception = assertThrows(ParticipationRequestNotFoundException.class, () -> commonComponent.partrequestExists(partRequestId));
        assertEquals(String.format(PARTICIPATION_REQUEST_NOT_FOUND, partRequestId), exception.getMessage());

        Mockito.verify(participationRequestRepository, Mockito.only()).existsById(partRequestId);
        Mockito.verifyNoMoreInteractions(participationRequestRepository);

        Mockito.verifyNoInteractions(onlyParticipationRequestRepo);
    }

    @Test
    public void test_T0180_PS01_getParticipationRequestById_contains() {
        Mockito.when(participationRequestRepository.findById(partRequestId))
                .thenReturn(Optional.of(request));

        ParticipationRequest gotParticipationRequest = commonComponent.getParticipationRequestById(partRequestId);

        assertNotNull(gotParticipationRequest);
        assertEquals(request.getId(), gotParticipationRequest.getId());
        assertEquals(request.getRequester().getId(), gotParticipationRequest.getRequester().getId());
        assertEquals(request.getEvent().getId(), gotParticipationRequest.getEvent().getId());

        Mockito.verify(participationRequestRepository, Mockito.only()).findById(partRequestId);
        Mockito.verifyNoMoreInteractions(participationRequestRepository);

        Mockito.verifyNoInteractions(onlyParticipationRequestRepo);
    }

    @Test
    public void test_T0180_NS01_getParticipationRequestById_notContains_throwsParticipationRequestNotFoundException() {
        Mockito.when(participationRequestRepository.findById(partRequestId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(ParticipationRequestNotFoundException.class, () -> commonComponent.getParticipationRequestById(partRequestId));
        assertEquals(String.format(PARTICIPATION_REQUEST_NOT_FOUND, partRequestId), exception.getMessage());

        Mockito.verify(participationRequestRepository, Mockito.only()).findById(partRequestId);
        Mockito.verifyNoMoreInteractions(participationRequestRepository);

        Mockito.verifyNoInteractions(onlyParticipationRequestRepo);
    }

    @Test
    public void test_T0190_PS01_containsLikeById_contains() {
        Mockito.when(likeRepository.existsById(LikeId.of(userId, eventId)))
                .thenReturn(true);

        boolean result = commonComponent.containsLikeById(userId, eventId);
        assertTrue(result);

        Mockito.verify(likeRepository, Mockito.only()).existsById(LikeId.of(userId, eventId));
        Mockito.verifyNoMoreInteractions(likeRepository);

        Mockito.verifyNoInteractions(onlyLikeRepo);
    }

    @Test
    public void test_T0190_PS02_containsLikeById_contains() {
        Mockito.when(likeRepository.existsById(LikeId.of(userId, eventId)))
                .thenReturn(true);

        boolean result = commonComponent.containsLikeById(LikeId.of(userId, eventId));
        assertTrue(result);

        Mockito.verify(likeRepository, Mockito.only()).existsById(LikeId.of(userId, eventId));
        Mockito.verifyNoMoreInteractions(likeRepository);

        Mockito.verifyNoInteractions(onlyLikeRepo);
    }

    @Test
    public void test_T0190_NS01_containsLikeById_notContains() {
        Mockito.when(likeRepository.existsById(LikeId.of(userId, eventId)))
                .thenReturn(false);

        boolean result = commonComponent.containsLikeById(userId, eventId);
        assertFalse(result);

        Mockito.verify(likeRepository, Mockito.only()).existsById(LikeId.of(userId, eventId));
        Mockito.verifyNoMoreInteractions(likeRepository);

        Mockito.verifyNoInteractions(onlyLikeRepo);
    }

    @Test
    public void test_T0190_NS02_containsLikeById_notContains() {
        Mockito.when(likeRepository.existsById(LikeId.of(userId, eventId)))
                .thenReturn(false);

        boolean result = commonComponent.containsLikeById(LikeId.of(userId, eventId));
        assertFalse(result);

        Mockito.verify(likeRepository, Mockito.only()).existsById(LikeId.of(userId, eventId));
        Mockito.verifyNoMoreInteractions(likeRepository);

        Mockito.verifyNoInteractions(onlyLikeRepo);
    }

    @Test
    public void test_T0200_PS01_likeExists_contains() {
        Mockito.when(likeRepository.existsById(LikeId.of(userId, eventId)))
                .thenReturn(true);

        assertDoesNotThrow(() -> commonComponent.likeExists(userId, eventId));

        Mockito.verify(likeRepository, Mockito.only()).existsById(LikeId.of(userId, eventId));
        Mockito.verifyNoMoreInteractions(likeRepository);

        Mockito.verifyNoInteractions(onlyLikeRepo);
    }

    @Test
    public void test_T0200_PS02_likeExists_contains() {
        Mockito.when(likeRepository.existsById(LikeId.of(userId, eventId)))
                .thenReturn(true);

        assertDoesNotThrow(() -> commonComponent.likeExists(LikeId.of(userId, eventId)));

        Mockito.verify(likeRepository, Mockito.only()).existsById(LikeId.of(userId, eventId));
        Mockito.verifyNoMoreInteractions(likeRepository);

        Mockito.verifyNoInteractions(onlyLikeRepo);
    }

    @Test
    public void test_T0200_NS01_likeExists_notContains_throwsLikeNotFoundException() {
        Mockito.when(likeRepository.existsById(LikeId.of(userId, eventId)))
                .thenReturn(false);

        RuntimeException exception = assertThrows(LikeNotFoundException.class, () -> commonComponent.likeExists(userId, eventId));
        assertEquals(String.format(LIKE_NOT_FOUND, userId, eventId), exception.getMessage());

        Mockito.verify(likeRepository, Mockito.only()).existsById(LikeId.of(userId, eventId));
        Mockito.verifyNoMoreInteractions(likeRepository);

        Mockito.verifyNoInteractions(onlyLikeRepo);
    }

    @Test
    public void test_T0200_NS02_likeExists_notContains_throwsLikeNotFoundException() {
        Mockito.when(likeRepository.existsById(LikeId.of(userId, eventId)))
                .thenReturn(false);

        RuntimeException exception = assertThrows(LikeNotFoundException.class, () -> commonComponent.likeExists(LikeId.of(userId, eventId)));
        assertEquals(String.format(LIKE_NOT_FOUND, userId, eventId), exception.getMessage());

        Mockito.verify(likeRepository, Mockito.only()).existsById(LikeId.of(userId, eventId));
        Mockito.verifyNoMoreInteractions(likeRepository);

        Mockito.verifyNoInteractions(onlyLikeRepo);
    }

    @Test
    public void test_T0210_PS01_getLikeById_contains() {
        Mockito.when(likeRepository.findById(LikeId.of(userId, eventId)))
                .thenReturn(Optional.of(like));

        Like gotLike = commonComponent.getLikeById(userId, eventId);

        assertNotNull(gotLike);
        assertEquals(like.getUserId(), gotLike.getUserId());
        assertEquals(like.getEventId(), gotLike.getEventId());
        assertEquals(like.getLike(), gotLike.getLike());

        Mockito.verify(likeRepository, Mockito.only()).findById(LikeId.of(userId, eventId));
        Mockito.verifyNoMoreInteractions(likeRepository);

        Mockito.verifyNoInteractions(onlyLikeRepo);
    }


    @Test
    public void test_T0210_PS02_getLikeById_contains() {
        Mockito.when(likeRepository.findById(LikeId.of(userId, eventId)))
                .thenReturn(Optional.of(like));

        Like gotLike = commonComponent.getLikeById(LikeId.of(userId, eventId));

        assertNotNull(gotLike);
        assertEquals(like.getUserId(), gotLike.getUserId());
        assertEquals(like.getEventId(), gotLike.getEventId());
        assertEquals(like.getLike(), gotLike.getLike());

        Mockito.verify(likeRepository, Mockito.only()).findById(LikeId.of(userId, eventId));
        Mockito.verifyNoMoreInteractions(likeRepository);

        Mockito.verifyNoInteractions(onlyLikeRepo);
    }

    @Test
    public void test_T0210_NS01_getLikeById_notContains_throwsLikeNotFoundException() {
        Mockito.when(likeRepository.findById(LikeId.of(userId, eventId)))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(LikeNotFoundException.class, () -> commonComponent.getLikeById(userId, eventId));
        assertEquals(String.format(LIKE_NOT_FOUND, userId, eventId), exception.getMessage());

        Mockito.verify(likeRepository, Mockito.only()).findById(LikeId.of(userId, eventId));
        Mockito.verifyNoMoreInteractions(likeRepository);

        Mockito.verifyNoInteractions(onlyLikeRepo);
    }

    @Test
    public void test_T0210_NS02_getLikeById_notContains_throwsLikeNotFoundException() {
        Mockito.when(likeRepository.findById(LikeId.of(userId, eventId)))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(LikeNotFoundException.class, () -> commonComponent.getLikeById(LikeId.of(userId, eventId)));
        assertEquals(String.format(LIKE_NOT_FOUND, userId, eventId), exception.getMessage());

        Mockito.verify(likeRepository, Mockito.only()).findById(LikeId.of(userId, eventId));
        Mockito.verifyNoMoreInteractions(likeRepository);

        Mockito.verifyNoInteractions(onlyLikeRepo);
    }

    @Test
    public void test_T0220_PS01_containsRatingById_contains() {
        Mockito.when(ratingRepository.existsById(RatingId.of(userId, eventId)))
                .thenReturn(true);

        boolean result = commonComponent.containsRatingById(userId, eventId);
        assertTrue(result);

        Mockito.verify(ratingRepository, Mockito.only()).existsById(RatingId.of(userId, eventId));
        Mockito.verifyNoMoreInteractions(ratingRepository);

        Mockito.verifyNoInteractions(onlyRatingRepo);
    }

    @Test
    public void test_T0220_PS02_containsRatingById_contains() {
        Mockito.when(ratingRepository.existsById(RatingId.of(userId, eventId)))
                .thenReturn(true);

        boolean result = commonComponent.containsRatingById(RatingId.of(userId, eventId));
        assertTrue(result);

        Mockito.verify(ratingRepository, Mockito.only()).existsById(RatingId.of(userId, eventId));
        Mockito.verifyNoMoreInteractions(ratingRepository);

        Mockito.verifyNoInteractions(onlyRatingRepo);
    }

    @Test
    public void test_T0220_NS01_containsRatingById_notContains() {
        Mockito.when(ratingRepository.existsById(RatingId.of(userId, eventId)))
                .thenReturn(false);

        boolean result = commonComponent.containsRatingById(userId, eventId);
        assertFalse(result);

        Mockito.verify(ratingRepository, Mockito.only()).existsById(RatingId.of(userId, eventId));
        Mockito.verifyNoMoreInteractions(ratingRepository);

        Mockito.verifyNoInteractions(onlyRatingRepo);
    }

    @Test
    public void test_T0220_NS02_containsRatingById_notContains() {
        Mockito.when(ratingRepository.existsById(RatingId.of(userId, eventId)))
                .thenReturn(false);

        boolean result = commonComponent.containsRatingById(RatingId.of(userId, eventId));
        assertFalse(result);

        Mockito.verify(ratingRepository, Mockito.only()).existsById(RatingId.of(userId, eventId));
        Mockito.verifyNoMoreInteractions(ratingRepository);

        Mockito.verifyNoInteractions(onlyRatingRepo);
    }

    @Test
    public void test_T0230_PS01_ratingExists_contains() {
        Mockito.when(ratingRepository.existsById(RatingId.of(userId, eventId)))
                .thenReturn(true);

        assertDoesNotThrow(() -> commonComponent.ratingExists(userId, eventId));

        Mockito.verify(ratingRepository, Mockito.only()).existsById(RatingId.of(userId, eventId));
        Mockito.verifyNoMoreInteractions(ratingRepository);

        Mockito.verifyNoInteractions(onlyRatingRepo);
    }

    @Test
    public void test_T0230_PS02_ratingExists_contains() {
        Mockito.when(ratingRepository.existsById(RatingId.of(userId, eventId)))
                .thenReturn(true);

        assertDoesNotThrow(() -> commonComponent.ratingExists(RatingId.of(userId, eventId)));

        Mockito.verify(ratingRepository, Mockito.only()).existsById(RatingId.of(userId, eventId));
        Mockito.verifyNoMoreInteractions(ratingRepository);

        Mockito.verifyNoInteractions(onlyRatingRepo);
    }

    @Test
    public void test_T0230_NS01_ratingExists_notContains_throwsRatingNotFoundException() {
        Mockito.when(ratingRepository.existsById(RatingId.of(userId, eventId)))
                .thenReturn(false);

        RuntimeException exception = assertThrows(RatingNotFoundException.class, () -> commonComponent.ratingExists(userId, eventId));
        assertEquals(String.format(RATING_NOT_FOUND, userId, eventId), exception.getMessage());

        Mockito.verify(ratingRepository, Mockito.only()).existsById(RatingId.of(userId, eventId));
        Mockito.verifyNoMoreInteractions(ratingRepository);

        Mockito.verifyNoInteractions(onlyRatingRepo);
    }

    @Test
    public void test_T0230_NS02_ratingExists_notContains_throwsRatingNotFoundException() {
        Mockito.when(ratingRepository.existsById(RatingId.of(userId, eventId)))
                .thenReturn(false);

        RuntimeException exception = assertThrows(RatingNotFoundException.class, () -> commonComponent.ratingExists(RatingId.of(userId, eventId)));
        assertEquals(String.format(RATING_NOT_FOUND, userId, eventId), exception.getMessage());

        Mockito.verify(ratingRepository, Mockito.only()).existsById(RatingId.of(userId, eventId));
        Mockito.verifyNoMoreInteractions(ratingRepository);

        Mockito.verifyNoInteractions(onlyRatingRepo);
    }

    @Test
    public void test_T0240_PS01_getRatingById_contains() {
        Mockito.when(ratingRepository.findById(RatingId.of(userId, eventId)))
                .thenReturn(Optional.of(rating));

        Rating gotRating = commonComponent.getRatingById(userId, eventId);

        assertNotNull(gotRating);
        assertEquals(rating.getUserId(), gotRating.getUserId());
        assertEquals(rating.getEventId(), gotRating.getEventId());
        assertEquals(rating.getRating(), gotRating.getRating());

        Mockito.verify(ratingRepository, Mockito.only()).findById(RatingId.of(userId, eventId));
        Mockito.verifyNoMoreInteractions(ratingRepository);

        Mockito.verifyNoInteractions(onlyRatingRepo);
    }


    @Test
    public void test_T0240_PS02_getRatingById_contains() {
        Mockito.when(ratingRepository.findById(RatingId.of(userId, eventId)))
                .thenReturn(Optional.of(rating));

        Rating gotRating = commonComponent.getRatingById(RatingId.of(userId, eventId));

        assertNotNull(gotRating);
        assertEquals(rating.getUserId(), gotRating.getUserId());
        assertEquals(rating.getEventId(), gotRating.getEventId());
        assertEquals(rating.getRating(), gotRating.getRating());

        Mockito.verify(ratingRepository, Mockito.only()).findById(RatingId.of(userId, eventId));
        Mockito.verifyNoMoreInteractions(ratingRepository);

        Mockito.verifyNoInteractions(onlyRatingRepo);
    }

    @Test
    public void test_T0240_NS01_getRatingById_notContains_throwsRatingNotFoundException() {
        Mockito.when(ratingRepository.findById(RatingId.of(userId, eventId)))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RatingNotFoundException.class, () -> commonComponent.getRatingById(userId, eventId));
        assertEquals(String.format(RATING_NOT_FOUND, userId, eventId), exception.getMessage());

        Mockito.verify(ratingRepository, Mockito.only()).findById(RatingId.of(userId, eventId));
        Mockito.verifyNoMoreInteractions(ratingRepository);

        Mockito.verifyNoInteractions(onlyRatingRepo);
    }

    @Test
    public void test_T0240_NS02_getRatingById_notContains_throwsRatingNotFoundException() {
        Mockito.when(ratingRepository.findById(RatingId.of(userId, eventId)))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RatingNotFoundException.class, () -> commonComponent.getRatingById(RatingId.of(userId, eventId)));
        assertEquals(String.format(RATING_NOT_FOUND, userId, eventId), exception.getMessage());

        Mockito.verify(ratingRepository, Mockito.only()).findById(RatingId.of(userId, eventId));
        Mockito.verifyNoMoreInteractions(ratingRepository);

        Mockito.verifyNoInteractions(onlyRatingRepo);
    }
}