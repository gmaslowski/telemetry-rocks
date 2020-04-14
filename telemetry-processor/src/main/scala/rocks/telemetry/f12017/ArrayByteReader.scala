package rocks.telemetry.f12017

import java.nio.ByteBuffer


object BasicTypesArrayByteReader {

  implicit class TelemetryPacketSimpleFunctions(val packet: Array[Byte]) {

    def getFloat: Int => Float = idx => ByteBuffer.wrap(packet.slice(idx, idx + 4).reverse).getFloat

    def getByte: Int => Byte = idx => ByteBuffer.wrap(packet).get(idx)

    def getByteArray: Int => Array[Byte] = idx => Array(packet.getByte(idx), packet.getByte(idx + 1), packet.getByte(idx + 2), packet.getByte(idx + 3))

    def getFloatArray: Int => Array[Float] = idx => Array(packet.getFloat(idx), packet.getFloat(idx + 4), packet.getFloat(idx + 8), packet.getFloat(idx + 12))
  }

}

object ArrayByteReader {

  implicit class TelemetryPacketFunctions(val packet: Array[Byte]) {

    import BasicTypesArrayByteReader._

    def m_time: Float = packet.getFloat(0)

    def m_lapTime: Float = packet.getFloat(4)

    def m_lapDistance: Float = packet.getFloat(8)

    def m_totalDistance: Float = packet.getFloat(12)

    def m_x: Float = packet.getFloat(16)

    def m_y: Float = packet.getFloat(20)

    def m_z: Float = packet.getFloat(24)

    def m_speed: Float = packet.getFloat(28)

    def m_xv: Float = packet.getFloat(32)

    def m_yv: Float = packet.getFloat(36)

    def m_zv: Float = packet.getFloat(40)

    def m_xr: Float = packet.getFloat(44)

    def m_yr: Float = packet.getFloat(48)

    def m_zr: Float = packet.getFloat(52)

    def m_xd: Float = packet.getFloat(56)

    def m_yd: Float = packet.getFloat(60)

    def m_zd: Float = packet.getFloat(64)

    def m_susp_pos: Wheels = Wheels(packet.getFloatArray(68))

    def m_susp_vel: Wheels = Wheels(packet.getFloatArray(84))

    def m_wheel_speed: Wheels = Wheels(packet.getFloatArray(100))

    def m_throttle: Float = packet.getFloat(116)

    def m_steer: Float = packet.getFloat(120)

    def m_brake: Float = packet.getFloat(124)

    def m_clutch: Float = packet.getFloat(128)

    def m_gear: Float = packet.getFloat(132)

    def m_gforce_lat: Float = packet.getFloat(136)

    def m_gforce_lon: Float = packet.getFloat(140)

    def m_lap: Float = packet.getFloat(144)

    def m_engineRate: Float = packet.getFloat(148)

    def m_sli_pro_native_support: Float = packet.getFloat(152)

    def m_car_position: Float = packet.getFloat(156)

    def m_kers_level: Float = packet.getFloat(160)

    def m_kers_max_level: Float = packet.getFloat(164)

    def m_drs: Float = packet.getFloat(168)

    def m_traction_control: Float = packet.getFloat(172)

    def m_anti_lock_brakes: Float = packet.getFloat(176)

    def m_fuel_in_tank: Float = packet.getFloat(180)

    def m_fuel_capacity: Float = packet.getFloat(184)

    def m_in_pits: Float = packet.getFloat(188)

    def m_sector: Float = packet.getFloat(192)

    def m_sector1_time: Float = packet.getFloat(196)

    def m_sector2_time: Float = packet.getFloat(200)

    def m_brakes_temp: Wheels = Wheels(packet.getFloatArray(204))

    def m_tyres_pressure: Wheels = Wheels(packet.getFloatArray(220))

    def m_team_info: Float = packet.getFloat(236)

    def m_total_laps: Float = packet.getFloat(240)

    def m_track_size: Float = packet.getFloat(244)

    def m_last_lap_time: Float = packet.getFloat(248)

    def m_max_rpm: Float = packet.getFloat(252)

    def m_idle_rpm: Float = packet.getFloat(256)

    def m_max_gears: Float = packet.getFloat(260)

    def m_sessionType: Float = packet.getFloat(264)

    def m_drsAllowed: Float = packet.getFloat(268)

    def m_track_number: Float = packet.getFloat(272)

    def m_vehicleFIAFlags: Float = packet.getFloat(276)

    def m_era: Float = packet.getFloat(280)

    def m_engine_temperature: Float = packet.getFloat(284)

    def m_gforce_vert: Float = packet.getFloat(288)

    def m_ang_vel_x: Float = packet.getFloat(292)

    def m_ang_vel_y: Float = packet.getFloat(296)

    def m_ang_vel_z: Float = packet.getFloat(300)

    def m_tyres_temperature: WheelsByte = WheelsByte(packet.getByteArray(304))

    def m_tyres_wear: WheelsByte = WheelsByte(packet.getByteArray(308))

    def m_tyre_compound: Byte = packet.getByte(312)

    def m_front_brake_bias: Byte = packet.getByte(313)

    def m_fuel_mix: Byte = packet.getByte(314)

    def m_currentLapInvalid: Byte = packet.getByte(315)

    def m_tyres_damage: WheelsByte = WheelsByte(packet.getByteArray(316))

    def m_front_left_wing_damage: Byte = packet.getByte(320)

    def m_front_right_wing_damage: Byte = packet.getByte(321)

    def m_rear_wing_damage: Byte = packet.getByte(322)

    def m_engine_damage: Byte = packet.getByte(323)

    def m_gear_box_damage: Byte = packet.getByte(324)

    def m_exhaust_damage: Byte = packet.getByte(325)

    def m_pit_limiter_status: Byte = packet.getByte(326)

    def m_pit_speed_limit: Byte = packet.getByte(327)

    def m_session_time_left: Float = packet.getFloat(328)

    def m_rev_lights_percent: Byte = packet.getByte(332)

    def m_is_spectating: Byte = packet.getByte(333)

    def m_spectator_car_index: Byte = packet.getByte(334)

    def m_num_cars: Byte = packet.getByte(335)

    def m_player_car_index: Byte = packet.getByte(336)

    def m_car_data: Array[PacketCarData] = ???

    def m_yaw: Float = packet.getFloat(1238)

    def m_pitch: Float = packet.getFloat(1242)

    def m_roll: Float = packet.getFloat(1246)

    def m_x_local_velocity: Float = packet.getFloat(1250)

    def m_y_local_velocity: Float = packet.getFloat(1254)

    def m_z_local_velocity: Float = packet.getFloat(1258)

    def m_susp_acceleration: Wheels = Wheels(packet.getFloatArray(1262))

    def m_ang_acc_x: Float = packet.getFloat(1278)

    def m_ang_acc_y: Float = packet.getFloat(1282)

    def m_ang_acc_z: Float = packet.getFloat(1286)
  }

}


object F1Enums {

  implicit class TyreImplicit(val enumVal: Int) {

    def asTyre: String =
      enumVal match {
        case 0 => "Ultra Soft"
        case 1 => "Super Soft"
        case 2 => "Soft"
        case 3 => "Medium"
        case 4 => "Hard"
        case 5 => "Inter"
        case 6 => "Wet"
      }

    def asTeam: String =
      enumVal match {
        case 4 => "Mercedes"
        case 0 => "Redbull"
        case 1 => "Ferrari"
        case 6 => "Force India"
        case 7 => "Williams"
        case 2 => "McLaren"
        case 8 => "Toro Rosso"
        case 11 => "Haas"
        case 3 => "Renault"
        case 5 => "Sauber"
      }
  }

}

case class PacketCarData(m_worldPosition: WorldPosition,
                         m_lastLapTime: Float,
                         m_currentLapTime: Float,
                         m_bestLapTime: Float,
                         m_sector1Time: Float,
                         m_sector2Time: Float,
                         m_lapDistance: Float,
                         m_driverId: Byte,
                         m_teamId: Byte,
                         m_carPosition: Byte,
                         m_currentLapNum: Byte,
                         m_tyreCompound: Byte,
                         m_inPits: Byte,
                         m_sector: Byte,
                         m_currentLapInvalid: Byte,
                         m_penalties: Byte)

case class WorldPosition(x: Float,
                         y: Float,
                         z: Float)

case class Wheels(array: Array[Float])

//  rearLeft: Float,
//                  rearRight: Float,
//                  frontLeft: Float,
//                  frontRight: Float)

case class WheelsByte(array: Array[Byte])

//                       rearLeft: Byte,
//                      rearRight: Byte,
//                      frontLeft: Byte,
//                      frontRight: Byte)
