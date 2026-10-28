package com.secretmissions.game;

import java.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

/** Ten distinct categories, six variants per category (60 authored objectives). */
public final class MissionCatalog {
    private MissionCatalog() {}
    public record Mission(String category, String title, String detail, String type, String target, int amount) {}
    public static final List<List<Mission>> CATEGORIES = List.of(
        List.of(
            m("Добыча", "Шахтёр", "Сломай 20 камня", "break", "minecraft:stone", 20),
            m("Добыча", "Угольная смена", "Сломай 6 угольной руды", "break", "minecraft:coal_ore", 6),
            m("Добыча", "Медная жила", "Сломай 6 медной руды", "break", "minecraft:copper_ore", 6),
            m("Добыча", "Глубокая добыча", "Сломай 12 глубинного сланца", "break", "minecraft:deepslate", 12),
            m("Добыча", "Гранитный карьер", "Сломай 16 гранита", "break", "minecraft:granite", 16),
            m("Добыча", "Песчаный карьер", "Сломай 24 песка", "break", "minecraft:sand", 24)),
        List.of(
            m("Крафт", "Верстачник", "Создай 2 сундука", "craft", "minecraft:chest", 2),
            m("Крафт", "Плотник", "Создай 8 палок", "craft", "minecraft:stick", 8),
            m("Крафт", "Стекольщик", "Создай 16 стеклянных панелей", "craft", "minecraft:glass_pane", 16),
            m("Крафт", "Оружейник", "Создай каменный меч", "craft", "minecraft:stone_sword", 1),
            m("Крафт", "Мастер печей", "Создай 2 печи", "craft", "minecraft:furnace", 2),
            m("Крафт", "Декоратор", "Создай 4 факела", "craft", "minecraft:torch", 4)),
        List.of(
            m("Сражение", "Охотник на зомби", "Убей 3 зомби", "kill", "minecraft:zombie", 3),
            m("Сражение", "Костолом", "Убей 2 скелетов", "kill", "minecraft:skeleton", 2),
            m("Сражение", "Паучий страх", "Убей 2 пауков", "kill", "minecraft:spider", 2),
            m("Сражение", "Взрывная работа", "Убей крипера", "kill", "minecraft:creeper", 1),
            m("Сражение", "Ночной патруль", "Убей 2 утопленников", "kill", "minecraft:drowned", 2),
            m("Сражение", "Пещерный бой", "Убей 3 летучих мышей", "kill", "minecraft:bat", 3)),
        List.of(
            m("Исследование", "Лесная прогулка", "Посети лес", "biome", "minecraft:forest", 1),
            m("Исследование", "Равнинный путь", "Посети равнину", "biome", "minecraft:plains", 1),
            m("Исследование", "Песчаный горизонт", "Посети пустыню", "biome", "minecraft:desert", 1),
            m("Исследование", "Хвойная тропа", "Посети тайгу", "biome", "minecraft:taiga", 1),
            m("Исследование", "Холодный маршрут", "Посети снежную равнину", "biome", "minecraft:snowy_plains", 1),
            m("Исследование", "Джунгли зовут", "Посети джунгли", "biome", "minecraft:jungle", 1)),
        List.of(
            m("Путешествие", "Пеший турист", "Пройди 500 блоков пешком", "walk", "", 500),
            m("Путешествие", "Длинная дорога", "Пройди 750 блоков пешком", "walk", "", 750),
            m("Путешествие", "Спринтер", "Пройди 300 блоков пешком", "walk", "", 300),
            m("Путешествие", "Марафон", "Пройди 1000 блоков пешком", "walk", "", 1000),
            m("Путешествие", "Разминка", "Пройди 200 блоков пешком", "walk", "", 200),
            m("Путешествие", "Поход", "Пройди 600 блоков пешком", "walk", "", 600)),
        List.of(
            m("Коллекция", "Собиратель яблок", "Держи в инвентаре 3 яблока", "inventory", "minecraft:apple", 3),
            m("Коллекция", "Каменные запасы", "Держи в инвентаре 32 булыжника", "inventory", "minecraft:cobblestone", 32),
            m("Коллекция", "Красный порошок", "Держи в инвентаре 8 редстоуна", "inventory", "minecraft:redstone", 8),
            m("Коллекция", "Костяной склад", "Держи в инвентаре 5 костей", "inventory", "minecraft:bone", 5),
            m("Коллекция", "Сладкая жизнь", "Держи в инвентаре 12 сахарного тростника", "inventory", "minecraft:sugar_cane", 12),
            m("Коллекция", "Запас дерева", "Держи в инвентаре 24 дубовых бревна", "inventory", "minecraft:oak_log", 24)),
        List.of(
            m("Выживание", "Без единой царапины", "Не получай урон 90 секунд", "safe", "", 90),
            m("Выживание", "Осторожный игрок", "Не получай урон 120 секунд", "safe", "", 120),
            m("Выживание", "Спокойствие", "Не получай урон 60 секунд", "safe", "", 60),
            m("Выживание", "Крепкий орешек", "Не получай урон 150 секунд", "safe", "", 150),
            m("Выживание", "Безопасный путь", "Не получай урон 75 секунд", "safe", "", 75),
            m("Выживание", "Невредимый", "Не получай урон 100 секунд", "safe", "", 100)),
        List.of(
            m("Фермерство", "Пшеничный урожай", "Собери 8 зрелой пшеницы", "crop", "minecraft:wheat", 8),
            m("Фермерство", "Морковная грядка", "Собери 6 зрелой моркови", "crop", "minecraft:carrots", 6),
            m("Фермерство", "Картофельный урожай", "Собери 6 зрелого картофеля", "crop", "minecraft:potatoes", 6),
            m("Фермерство", "Свекольная грядка", "Собери 5 зрелой свёклы", "crop", "minecraft:beetroots", 5),
            m("Фермерство", "Арбузная ферма", "Сломай 4 арбузных блока", "break", "minecraft:melon", 4),
            m("Фермерство", "Тыквенная ферма", "Сломай 4 тыквы", "break", "minecraft:pumpkin", 4)),
        List.of(
            m("Животные", "Овечий пастух", "Подойди к 3 разным овцам", "meet", "minecraft:sheep", 3),
            m("Животные", "Коровий пастух", "Подойди к 3 разным коровам", "meet", "minecraft:cow", 3),
            m("Животные", "Свиной пастух", "Подойди к 3 разным свиньям", "meet", "minecraft:pig", 3),
            m("Животные", "Куриный пастух", "Подойди к 4 разным курицам", "meet", "minecraft:chicken", 4),
            m("Животные", "Коневод", "Подойди к 2 разным лошадям", "meet", "minecraft:horse", 2),
            m("Животные", "Пчеловод", "Подойди к 2 разным пчёлам", "meet", "minecraft:bee", 2)),
        List.of(
            m("Строительство", "Дубовый строитель", "Поставь 20 дубовых досок", "place", "minecraft:oak_planks", 20),
            m("Строительство", "Каменная стена", "Поставь 16 булыжника", "place", "minecraft:cobblestone", 16),
            m("Строительство", "Освещение", "Поставь 12 факелов", "place", "minecraft:torch", 12),
            m("Строительство", "Стеклянный дом", "Поставь 12 стекла", "place", "minecraft:glass", 12),
            m("Строительство", "Кирпичный мастер", "Поставь 12 каменных кирпичей", "place", "minecraft:stone_bricks", 12),
            m("Строительство", "Уютный пол", "Поставь 16 еловых досок", "place", "minecraft:spruce_planks", 16))
    );
    private static Mission m(String cat,String title,String detail,String type,String target,int amount) { return new Mission(cat,title,detail,type,target,amount); }
    public static List<Mission> generate(Random random) {
        List<Mission> result = new ArrayList<>();
        for (List<Mission> category : CATEGORIES) result.add(category.get(random.nextInt(category.size())));
        Collections.shuffle(result, random);
        return result;
    }
}
