package domain.model.home.tariffs_by_location

data class PackageTariffCheckable(
    val tariff: DataTariffs,
    val isPackageChecked: Boolean = true,
    val locationName: String, // используется только для п. Литега
    val ktv: Boolean // используется только для п. Литега
)
