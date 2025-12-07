import com.daniloscataloni.liftking.viewmodels.ExerciseCreationViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::ExerciseCreationViewModel)
}