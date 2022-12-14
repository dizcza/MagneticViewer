/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ua.dronesapper.tcpdataviewer

object SharedKey {
    const val SERVER_IPADDR = "server_ipaddr"
    const val SERVER_PORT = "server_port"

    const val DATA_TYPE = "data_type"
    const val ENDIAN = "data_endian"

    const val PLOT_KEEP_LAST_COUNT = "plot_size"
    const val PLOT_UPDATE_PERIOD = "plot_update_period"
}

/**
 * Must match with the order of radio buttons in dialog_data_type.xml
 */
object DataTypeIndex {
    const val BYTE = 0
    const val SHORT = 1
    const val INT = 2
    const val LONG = 3
    const val FLOAT = 4
    const val DOUBLE = 5
}

/**
 * Defines several constants used between the service and UI.
 */
object Constants {
    const val BUFFER_SIZE = 1024
    const val RECORDS_FOLDER = "SensorRecords"
    const val SHARED_KEY_FILE = "ua.dronesapper.tcpdataviewer.SHARED_KEY"
}