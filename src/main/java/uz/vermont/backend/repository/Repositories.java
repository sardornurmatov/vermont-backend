package uz.vermont.backend.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import uz.vermont.backend.model.*;
import java.math.BigDecimal;
import java.util.*;

public final class Repositories { private Repositories() {} }

interface BaseMarker {}
