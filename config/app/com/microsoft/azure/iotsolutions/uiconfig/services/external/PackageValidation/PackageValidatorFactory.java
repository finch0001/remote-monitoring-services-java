package com.microsoft.azure.iotsolutions.uiconfig.services.external.PackageValidation;

import com.microsoft.azure.iotsolutions.uiconfig.services.models.PackageConfigType;
import com.microsoft.azure.iotsolutions.uiconfig.services.models.PackageType;

public class PackageValidatorFactory
{
    public static IPackageValidator GetValidator(PackageType packageType, String config)
    {
        if (packageType.equals(PackageType.edgeManifest))
        {
            return new EdgePackageValidator();
        }

        if (config.equalsIgnoreCase(PackageConfigType.firmwareUpdateMxChip.toString()))
        {
            return new FirmwareUpdateMxChipValidator();
        }
        else
        {
            return null;
        }
    }
}