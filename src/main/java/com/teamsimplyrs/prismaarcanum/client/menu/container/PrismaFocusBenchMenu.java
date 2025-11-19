package com.teamsimplyrs.prismaarcanum.client.menu.container;

import com.teamsimplyrs.prismaarcanum.api.casting.AbstractCastable;
import com.teamsimplyrs.prismaarcanum.api.utils.SpellUtils;
import com.teamsimplyrs.prismaarcanum.block.blockentity.PrismaFocusBenchBlockEntity;
import com.teamsimplyrs.prismaarcanum.item.SpellPrismItem;
import com.teamsimplyrs.prismaarcanum.network.payload.OnPrismConvergePayload;
import com.teamsimplyrs.prismaarcanum.registry.PABlockRegistry;
import com.teamsimplyrs.prismaarcanum.registry.PAMenuTypesRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class PrismaFocusBenchMenu extends AbstractContainerMenu {
    public final PrismaFocusBenchBlockEntity blockEntity;
    private final Level level;

    private static final int WAND_SLOT_INDEX = 36, MAX_SPELL_SLOTS = 5;

    public PrismaFocusBenchMenu(int containerId, Inventory playerInv, FriendlyByteBuf extraData) {
        this(containerId, playerInv, playerInv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public PrismaFocusBenchMenu(int containerId, Inventory playerInv, BlockEntity blockEntity) {
        super(PAMenuTypesRegistry.PRISMA_FOCUS_BENCH_MENU.get(), containerId);

        this.blockEntity = (PrismaFocusBenchBlockEntity) blockEntity;
        this.level = playerInv.player.level();

        addPlayerInventory(playerInv);
        addPlayerHotbar(playerInv);
        addPrismFocusBenchSlots();
    }

    private boolean isSlotValidForItem(int slotIndex, ItemStack stack) {
        boolean isValid;
        switch (slotIndex) {
            case 0:
                isValid = stack.getItem() instanceof AbstractCastable;
                break;
            default:
                isValid = stack.getItem() instanceof SpellPrismItem;
                break;
        }

        return isValid;
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 5;  // must be the number of slots you have!
    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, PABlockRegistry.PRISMA_FOCUS_BENCH.get());
    }

    private void addPrismFocusBenchSlots() {
        addSlot(new SlotItemHandler(this.blockEntity.inventory, 0, 80, 20){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return isSlotValidForItem(0, stack) && super.mayPlace(stack);
            }
        });

        addSlot(new SlotItemHandler(this.blockEntity.inventory, 1, 53, -7){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return isSlotValidForItem(1, stack) && super.mayPlace(stack);
            }
        });
        addSlot(new SlotItemHandler(this.blockEntity.inventory, 2, 108, -7){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return isSlotValidForItem(2, stack) && super.mayPlace(stack);
            }
        });
        addSlot(new SlotItemHandler(this.blockEntity.inventory, 3, 53, 47){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return isSlotValidForItem(3, stack) && super.mayPlace(stack);
            }
        });
        addSlot(new SlotItemHandler(this.blockEntity.inventory, 4, 108, 47){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return isSlotValidForItem(4, stack) && super.mayPlace(stack);
            }
        });
    }

    private void addPlayerInventory(Inventory playerInv) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(playerInv, j + (i*9) + 9, 12 + (j * 17), 117 + (i*18)));
            }
        }
    }

    private void addPlayerHotbar (Inventory playerInv) {
        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInv, i, 12 + (i*17), 176));
        }
    }

    public void onConvergePress() {
        List<ResourceLocation> spellIDs = new ArrayList<>();
        for (int i = WAND_SLOT_INDEX + 1; i < this.slots.size(); i++) {
            if (!this.getSlot(i).hasItem()) {
                continue;
            }

            ItemStack stackInSlot = this.getSlot(i).getItem();
            if (stackInSlot.getItem() instanceof SpellPrismItem) {
                ResourceLocation spellID = SpellUtils.checkPrismItemStackForSpell(stackInSlot);
                if (spellID != null) {
                    spellIDs.add(spellID);
                }
            }
        }
        PacketDistributor.sendToServer(new OnPrismConvergePayload(WAND_SLOT_INDEX, spellIDs));
    }
}
